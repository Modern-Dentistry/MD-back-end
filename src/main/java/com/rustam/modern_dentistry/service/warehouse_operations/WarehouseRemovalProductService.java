package com.rustam.modern_dentistry.service.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.enums.status.PendingStatus;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouse;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouseProduct;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseRemoval;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseRemovalProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_operations.WarehouseRemovalProductRepository;
import com.rustam.modern_dentistry.dto.OutOfTheWarehouseDto;
import com.rustam.modern_dentistry.dto.request.create.WarehouseRemovalCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.WarehouseRemovalProductCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.WarehouseRemovalProductSearchRequest;
import com.rustam.modern_dentistry.dto.response.create.WarehouseRemovalCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.WarehouseRemovalProductReadResponse;
import com.rustam.modern_dentistry.exception.custom.AmountSendException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.util.UtilService;
import com.rustam.modern_dentistry.util.specification.warehouse_operations.WarehouseRemovalProductSpecification;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WarehouseRemovalProductService {

    WarehouseRemovalProductRepository warehouseRemovalProductRepository;
    WarehouseRemovalService warehouseRemovalService;
    UtilService utilService;
    OrderFromWarehouseProductService orderFromWarehouseProductService;

    @Transactional
    public WarehouseRemovalCreateResponse create(WarehouseRemovalCreateRequest request) {
        WarehouseRemoval warehouseRemoval = warehouseRemovalService.findById(request.getWarehouseRemovalId());

        List<OutOfTheWarehouseDto> outOfTheWarehouseDtos = processWarehouseRemovalRequests(request, warehouseRemoval);

        return WarehouseRemovalCreateResponse.builder()
                .id(warehouseRemoval.getId())
                .date(request.getDate())
                .time(request.getTime())
                .description(request.getDescription())
                .outOfTheWarehouseDtos(outOfTheWarehouseDtos)
                .number(warehouseRemoval.getNumber())
                .status(PendingStatus.WAITING)
                .build();
    }

    private List<OutOfTheWarehouseDto> processWarehouseRemovalRequests(WarehouseRemovalCreateRequest request, WarehouseRemoval warehouseRemoval) {
        List<OutOfTheWarehouseDto> dtos = new ArrayList<>();

        for (WarehouseRemovalProductCreateRequest requestDetail : request.getRequests()) {
            // Məhsulu tapıb validasiya edirik
            var matchedProduct = findAndValidateProduct(warehouseRemoval, requestDetail);

            // Məhsulu yeniləyirik
            updateWarehouseProductQuantity(matchedProduct, requestDetail);

            // Yeni WarehouseRemovalProduct əlavə edilir
            WarehouseRemovalProduct warehouseRemovalProduct = addWarehouseRemovalProductEntity(warehouseRemoval, matchedProduct, requestDetail, request);

            // DTO yaradılıb cavab siyahısına əlavə olunur
            dtos.add(prepareOutOfTheWarehouseDto(warehouseRemovalProduct));
        }

        return dtos;
    }

    private OrderFromWarehouseProduct findAndValidateProduct(WarehouseRemoval warehouseRemoval, WarehouseRemovalProductCreateRequest requestDetail) {
        OrderFromWarehouse order = utilService.findByOrderFromWarehouseId(warehouseRemoval.getOrderFromWarehouse().getId());

        return order.getOrderFromWarehouseProducts().stream()
                .filter(product -> product.getId().equals(requestDetail.getOrderFromWarehouseProductId()))
                .findFirst()
                .orElseThrow(() ->
                        new NotFoundException("Product with ID: " + requestDetail.getOrderFromWarehouseProductId() + " not found."));
    }

    private void updateWarehouseProductQuantity(OrderFromWarehouseProduct matchedProduct, WarehouseRemovalProductCreateRequest requestDetail) {
        if (matchedProduct.getQuantity() < requestDetail.getCurrentExpenses()) {
            throw new AmountSendException("The amount sent is too much for product ID: " + requestDetail.getOrderFromWarehouseProductId());
        }

        long remainingQuantity = matchedProduct.getQuantity() - requestDetail.getCurrentExpenses();
        matchedProduct.setQuantity(remainingQuantity);
    }

    private OutOfTheWarehouseDto prepareOutOfTheWarehouseDto(WarehouseRemovalProduct warehouseRemovalProduct) {
        return OutOfTheWarehouseDto.builder()
                .categoryName(warehouseRemovalProduct.getCategoryName())
                .productName(warehouseRemovalProduct.getProductName())
                .productDescription(warehouseRemovalProduct.getProductDescription())
                .orderQuantity(warehouseRemovalProduct.getOrderAmount())
                .remainingQuantity(warehouseRemovalProduct.getRemainingAmount())
                .sendQuantity(warehouseRemovalProduct.getSendAmount())
                .currentAmount(warehouseRemovalProduct.getCurrentAmount())
                .build();
    }

    private WarehouseRemovalProduct addWarehouseRemovalProductEntity(WarehouseRemoval warehouseRemoval, OrderFromWarehouseProduct matchedProduct, WarehouseRemovalProductCreateRequest requestDetail, WarehouseRemovalCreateRequest request) {
        long sendAmount = 0L;

        List<WarehouseRemovalProduct> existingWarehouseRemovalProducts = warehouseRemovalProductRepository.findAllByWarehouseRemovalIdAndOrderFromWarehouseProductId(
                warehouseRemoval.getId(),
                requestDetail.getOrderFromWarehouseProductId()
        );

        if (existingWarehouseRemovalProducts != null && !existingWarehouseRemovalProducts.isEmpty()) {
            for (WarehouseRemovalProduct product : existingWarehouseRemovalProducts) {
                sendAmount += product.getSendAmount();
            }
            sendAmount += requestDetail.getCurrentExpenses();
        } else {
            sendAmount = requestDetail.getCurrentExpenses();
        }

        WarehouseRemovalProduct warehouseRemovalProduct = WarehouseRemovalProduct.builder()
                .categoryId(matchedProduct.getCategoryId())
                .productId(matchedProduct.getProductId())
                .currentAmount(requestDetail.getCurrentExpenses())
                .orderAmount(matchedProduct.getInitialQuantity())
                .remainingAmount(matchedProduct.getQuantity() - requestDetail.getCurrentExpenses())
                .sendAmount(sendAmount)
                .productName(matchedProduct.getProductName())
                .categoryName(matchedProduct.getCategoryName())
                .productDescription(request.getDescription() != null ? request.getDescription() : matchedProduct.getProductTitle())
                .orderFromWarehouseProductId(requestDetail.getOrderFromWarehouseProductId())
                .date(request.getDate() != null ? request.getDate() : warehouseRemoval.getDate())
                .time(request.getTime() != null ? request.getTime() : warehouseRemoval.getTime())
                .pendingStatus(PendingStatus.WAITING)
                .warehouseRemoval(warehouseRemoval)
                .build();

        warehouseRemoval.getWarehouseRemovalProducts().add(warehouseRemovalProduct);

        long newSendAmount = warehouseRemoval.getSendAmount() + requestDetail.getCurrentExpenses();
        warehouseRemoval.setSendAmount(newSendAmount);

        long newRemainingAmount = warehouseRemoval.getOrderFromWarehouse().getSumQuantity() - newSendAmount;
        warehouseRemoval.setRemainingAmount(newRemainingAmount);

        warehouseRemovalProductRepository.save(warehouseRemovalProduct);
        return warehouseRemovalProduct;
    }

    @Transactional
    public void deleteWarehouseRemovalIdBasedOnWarehouseRemovalProduct(Long id, Long warehouseRemovalProductId) {
        WarehouseRemoval warehouseRemoval = warehouseRemovalService.findById(id);

        WarehouseRemovalProduct warehouseRemovalProduct = warehouseRemoval.getWarehouseRemovalProducts().stream()
                .filter(product -> product.getId().equals(warehouseRemovalProductId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("WarehouseRemovalProduct with ID " + warehouseRemovalProductId + " not found in this WarehouseRemoval with ID " + id));

        if (!PendingStatus.WAITING.equals(warehouseRemovalProduct.getPendingStatus())) {
            throw new IllegalStateException("Cannot delete WarehouseRemovalProduct with ID " + warehouseRemovalProductId + " because it is already " + warehouseRemovalProduct.getPendingStatus());
        }

        restoreProductToWarehouseInventory(warehouseRemovalProduct);

        long updatedSendAmount = warehouseRemoval.getSendAmount() - warehouseRemovalProduct.getCurrentAmount();
        warehouseRemoval.setSendAmount(updatedSendAmount);

        long updatedRemainingAmount = warehouseRemoval.getOrderFromWarehouse().getSumQuantity() - updatedSendAmount;
        warehouseRemoval.setRemainingAmount(updatedRemainingAmount);

        warehouseRemoval.getWarehouseRemovalProducts().remove(warehouseRemovalProduct);
        warehouseRemovalProductRepository.delete(warehouseRemovalProduct);
    }

    private void restoreProductToWarehouseInventory(WarehouseRemovalProduct warehouseRemovalProduct) {
        OrderFromWarehouseProduct orderFromWarehouseProduct = orderFromWarehouseProductService.findById(warehouseRemovalProduct.getOrderFromWarehouseProductId());

        long restoredQuantity = orderFromWarehouseProduct.getQuantity() + warehouseRemovalProduct.getCurrentAmount();
        orderFromWarehouseProduct.setQuantity(restoredQuantity);

        orderFromWarehouseProductService.saveOrderFromWarehouseProduct(orderFromWarehouseProduct);
    }

    @Transactional
    public List<WarehouseRemovalProductReadResponse> read() {
        List<WarehouseRemovalProduct> warehouseRemovalProducts = warehouseRemovalProductRepository.findAll();

        return warehouseRemovalProducts.stream()
                .map(this::mapToReadResponse)
                .collect(Collectors.toList());
    }

    private WarehouseRemovalProductReadResponse mapToReadResponse(WarehouseRemovalProduct warehouseRemovalProduct) {
        Integer warehouseRemovalNumber = warehouseRemovalProduct.getWarehouseRemoval().getNumber();

        return WarehouseRemovalProductReadResponse.builder()
                .date(warehouseRemovalProduct.getDate())
                .time(warehouseRemovalProduct.getTime())
                .Id(warehouseRemovalProduct.getId())
                .pendingStatus(warehouseRemovalProduct.getPendingStatus())
                .number(warehouseRemovalNumber)
                .build();
    }

    @Transactional
    public List<WarehouseRemovalProductReadResponse> search(WarehouseRemovalProductSearchRequest warehouseRemovalProductSearchRequest) {
        List<WarehouseRemovalProduct> warehouseRemovalProducts = warehouseRemovalProductRepository.findAll(WarehouseRemovalProductSpecification.filterBy(warehouseRemovalProductSearchRequest));
        return warehouseRemovalProducts.stream()
                .map(this::mapToReadResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public WarehouseRemovalCreateResponse info(Long id) {
        // `WarehouseRemovalProduct` obyektini tapırıq
        WarehouseRemovalProduct warehouseRemovalProduct = findById(id);

        // Üst obyekt `WarehouseRemoval`-u tapırıq (əgər lazımdırsa)
        WarehouseRemoval warehouseRemoval = warehouseRemovalService.findById(
                warehouseRemovalProduct.getWarehouseRemoval().getId()
        );

        // Məhsul detallarını doldururuq
        List<OutOfTheWarehouseDto> outOfTheWarehouseDtos = warehouseRemoval.getWarehouseRemovalProducts().stream()
                .map(product -> OutOfTheWarehouseDto.builder()
                        .categoryName(product.getCategoryName()) // Kateqoriya adı
                        .productName(product.getProductName()) // Məhsul adı
                        .productDescription(product.getProductDescription()) // Məhsul təsviri
                        .orderQuantity(product.getOrderAmount()) // Sifariş edilmiş miqdar
                        .remainingQuantity(product.getRemainingAmount()) // Qalan miqdar
                        .sendQuantity(product.getSendAmount())
                        .currentAmount(product.getCurrentAmount()) // Cari miqdar
                        .build())
                .collect(Collectors.toList());

        // Final cavab obyektini quraşdırırıq
        return WarehouseRemovalCreateResponse.builder()
                .id(warehouseRemovalProduct.getId()) // `WarehouseRemoval` ID
                .date(warehouseRemovalProduct.getDate()) // Tarix
                .time(warehouseRemovalProduct.getTime()) // Saat
                .description(warehouseRemovalProduct.getProductDescription()) // Təsvir
                .number(warehouseRemoval.getNumber()) // Nömrə
                .status(warehouseRemovalProduct.getPendingStatus()) // Status
                .outOfTheWarehouseDtos(outOfTheWarehouseDtos) // Məhsul detalları
                .build();
    }

    public WarehouseRemovalProduct findById(Long id) {
        return warehouseRemovalProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("WarehouseRemovalProduct with ID " + id + " not found."));
    }
}
