package com.rustam.modern_dentistry.service.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.enums.status.PendingStatus;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouse;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouseProduct;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseRemoval;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseRemovalProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_operations.WarehouseRemovalProductRepository;
import com.rustam.modern_dentistry.dto.OutOfTheWarehouseDto;
import com.rustam.modern_dentistry.dto.record.ProcessedWarehouseRemoval;
import com.rustam.modern_dentistry.dto.request.create.WarehouseRemovalCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.WarehouseRemovalProductCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.WarehouseRemovalProductSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.WarehouseRemovalProductRequest;
import com.rustam.modern_dentistry.dto.request.update.WarehouseRemovalProductUpdateRequest;
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
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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

        ProcessedWarehouseRemoval processed = processWarehouseRemovalRequests(request, warehouseRemoval);

        return WarehouseRemovalCreateResponse.builder()
                .groupId(processed.groupId())
                .date(request.getDate())
                .time(request.getTime())
                .description(request.getDescription())
                .outOfTheWarehouseDtos(processed.dtos())
                .number(processed.dtos().size())
                .status(PendingStatus.WAITING)
                .build();
    }

    private ProcessedWarehouseRemoval processWarehouseRemovalRequests(WarehouseRemovalCreateRequest request, WarehouseRemoval warehouseRemoval) {
        List<OutOfTheWarehouseDto> dtos = new ArrayList<>();
        String groupId = utilService.generateGroupId();
        Integer number = request.getRequests().size();

        for (WarehouseRemovalProductCreateRequest requestDetail : request.getRequests()) {
            var matchedProduct = findAndValidateProduct(warehouseRemoval, requestDetail);
            updateWarehouseProductQuantity(matchedProduct, requestDetail);

            WarehouseRemovalProduct warehouseRemovalProduct = addWarehouseRemovalProductEntity(
                    warehouseRemoval, matchedProduct, requestDetail, request, groupId, number
            );

            dtos.add(prepareOutOfTheWarehouseDto(warehouseRemovalProduct));
        }

        return new ProcessedWarehouseRemoval(groupId, dtos);
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

    private WarehouseRemovalProduct addWarehouseRemovalProductEntity(
            WarehouseRemoval warehouseRemoval,
            OrderFromWarehouseProduct matchedProduct,
            WarehouseRemovalProductCreateRequest requestDetail,
            WarehouseRemovalCreateRequest request, String groupId, Integer number) {

        long currentSendAmount = calculateTotalSendAmount(warehouseRemoval.getId(), requestDetail);
        long remainingAmount = matchedProduct.getInitialQuantity() - currentSendAmount;

        WarehouseRemovalProduct warehouseRemovalProduct = buildWarehouseRemovalProduct(
                warehouseRemoval, matchedProduct, requestDetail, request, currentSendAmount, remainingAmount,groupId
                ,number
        );

        updateWarehouseRemoval(warehouseRemoval, requestDetail.getCurrentExpenses());

        warehouseRemovalProductRepository.save(warehouseRemovalProduct);
        warehouseRemoval.getWarehouseRemovalProducts().add(warehouseRemovalProduct);

        return warehouseRemovalProduct;
    }

    private long calculateTotalSendAmount(Long removalId, WarehouseRemovalProductCreateRequest requestDetail) {
        long previous = warehouseRemovalProductRepository
                .findAllByWarehouseRemovalIdAndOrderFromWarehouseProductId(removalId, requestDetail.getOrderFromWarehouseProductId())
                .stream()
                .mapToLong(WarehouseRemovalProduct::getCurrentAmount)
                .sum();

        return previous + requestDetail.getCurrentExpenses();
    }

    private WarehouseRemovalProduct buildWarehouseRemovalProduct(
            WarehouseRemoval warehouseRemoval,
            OrderFromWarehouseProduct matchedProduct,
            WarehouseRemovalProductCreateRequest requestDetail,
            WarehouseRemovalCreateRequest request,
            long currentSendAmount,
            long remainingAmount, String groupId, Integer number) {

        return WarehouseRemovalProduct.builder()
                .categoryId(matchedProduct.getCategoryId())
                .productId(matchedProduct.getProductId())
                .currentAmount(requestDetail.getCurrentExpenses())
                .orderAmount(matchedProduct.getInitialQuantity())
                .remainingAmount(remainingAmount)
                .sendAmount(currentSendAmount)
                .groupId(groupId)
                .number(number)
                .productName(matchedProduct.getProductName())
                .categoryName(matchedProduct.getCategoryName())
                .productDescription(Optional.ofNullable(request.getDescription()).orElse(matchedProduct.getProductTitle()))
                .orderFromWarehouseProductId(requestDetail.getOrderFromWarehouseProductId())
                .date(Optional.ofNullable(request.getDate()).orElse(warehouseRemoval.getDate()))
                .time(Optional.ofNullable(request.getTime()).orElse(warehouseRemoval.getTime()))
                .pendingStatus(PendingStatus.WAITING)
                .warehouseRemoval(warehouseRemoval)
                .build();
    }

    private void updateWarehouseRemoval(WarehouseRemoval warehouseRemoval, long currentExpense) {
        long updatedSendAmount = warehouseRemoval.getSendAmount() + currentExpense;
        warehouseRemoval.setSendAmount(updatedSendAmount);

        long updatedRemainingAmount = warehouseRemoval.getOrderFromWarehouse().getSumQuantity() - updatedSendAmount;
        warehouseRemoval.setRemainingAmount(updatedRemainingAmount);
    }

    @Transactional
    public void deleteWarehouseRemovalIdBasedOnWarehouseRemovalProduct(String groupId) {
        List<WarehouseRemovalProduct> productsToDelete = findAllByGroupId(groupId);

        if (productsToDelete.isEmpty()) {
            throw new NotFoundException("No WarehouseRemovalProduct found with groupId " + groupId);
        }

        for (WarehouseRemovalProduct product : productsToDelete) {
            if (!PendingStatus.WAITING.equals(product.getPendingStatus())) {
                throw new IllegalStateException("Cannot delete product with ID " + product.getId()
                        + " because it is already " + product.getPendingStatus());
            }

            restoreProductToWarehouseInventory(product);

            WarehouseRemoval warehouseRemoval = product.getWarehouseRemoval();

            long updatedSendAmount = warehouseRemoval.getSendAmount() - product.getCurrentAmount();
            warehouseRemoval.setSendAmount(updatedSendAmount);

            long updatedRemainingAmount = warehouseRemoval.getOrderFromWarehouse().getSumQuantity() - updatedSendAmount;
            warehouseRemoval.setRemainingAmount(updatedRemainingAmount);

            warehouseRemoval.getWarehouseRemovalProducts().remove(product);

            warehouseRemovalProductRepository.delete(product);
        }
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
        return WarehouseRemovalProductReadResponse.builder()
                .date(warehouseRemovalProduct.getDate())
                .time(warehouseRemovalProduct.getTime())
                .Id(warehouseRemovalProduct.getId())
                .pendingStatus(warehouseRemovalProduct.getPendingStatus())
                .number(warehouseRemovalProduct.getNumber())
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
    public WarehouseRemovalCreateResponse info(String groupId) {
        List<WarehouseRemovalProduct> products = findAllByGroupId(groupId);
        if (products.isEmpty()) {
            throw new NotFoundException("No WarehouseRemovalProducts found for groupId: " + groupId);
        }

        WarehouseRemovalProduct sampleProduct = products.get(0);

        List<OutOfTheWarehouseDto> outOfTheWarehouseDtos = products.stream()
                .map(product -> OutOfTheWarehouseDto.builder()
                        .categoryName(product.getCategoryName())
                        .productName(product.getProductName())
                        .productDescription(product.getProductDescription())
                        .orderQuantity(product.getOrderAmount())
                        .remainingQuantity(product.getRemainingAmount())
                        .sendQuantity(product.getSendAmount())
                        .currentAmount(product.getCurrentAmount())
                        .build())
                .collect(Collectors.toList());

        return WarehouseRemovalCreateResponse.builder()
                .id(sampleProduct.getId())
                .date(sampleProduct.getDate())
                .time(sampleProduct.getTime())
                .description(sampleProduct.getProductDescription())
                .number(sampleProduct.getNumber())
                .status(sampleProduct.getPendingStatus())
                .outOfTheWarehouseDtos(outOfTheWarehouseDtos)
                .build();
    }


    public WarehouseRemovalProduct findById(Long id) {
        return warehouseRemovalProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("WarehouseRemovalProduct with ID " + id + " not found."));
    }

    public List<WarehouseRemovalProduct> findAllByGroupId(String groupId) {
        return warehouseRemovalProductRepository.findAllByGroupId(groupId);
    }

    @Transactional
    public WarehouseRemovalCreateResponse update(WarehouseRemovalProductUpdateRequest request) {
        List<WarehouseRemovalProduct> existingProducts = findAllByGroupId(request.getGroupId());

        if (existingProducts.isEmpty()) {
            throw new NotFoundException("GroupId-yə uyğun WarehouseRemovalProduct tapılmadı: " + request.getGroupId());
        }

        List<OutOfTheWarehouseDto> updatedDtos = new ArrayList<>();

        for (WarehouseRemovalProductRequest req : request.getRequests()) {
            WarehouseRemovalProduct matchedProduct = existingProducts.stream()
                    .filter(p -> p.getOrderFromWarehouseProductId().equals(req.getOrderFromWarehouseProductId()))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException(
                            "OrderFromWarehouseProduct ID-si uyğun məhsul tapılmadı: " + req.getOrderFromWarehouseProductId()
                    ));

            utilService.updateFieldIfPresent(req.getCurrentExpenses(), matchedProduct::setCurrentAmount);
            utilService.updateFieldIfPresent(request.getDescription(), matchedProduct::setProductDescription);
            utilService.updateFieldIfPresent(request.getDate(), matchedProduct::setDate);
            utilService.updateFieldIfPresent(request.getTime(), matchedProduct::setTime);

            warehouseRemovalProductRepository.save(matchedProduct);

            updatedDtos.add(prepareOutOfTheWarehouseDto(matchedProduct));
        }
        WarehouseRemovalProduct sample = existingProducts.get(0);
        return WarehouseRemovalCreateResponse.builder()
                .id(sample.getId())
                .groupId(sample.getGroupId())
                .date(request.getDate())
                .time(request.getTime())
                .description(request.getDescription())
                .number(updatedDtos.size())
                .status(sample.getPendingStatus())
                .outOfTheWarehouseDtos(updatedDtos)
                .build();
    }

}
