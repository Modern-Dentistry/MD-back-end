package com.rustam.modern_dentistry.service.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.enums.status.PendingStatus;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouse;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouseProduct;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseRemoval;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseRemovalProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_operations.WarehouseRemovalRepository;
import com.rustam.modern_dentistry.dto.OutOfTheWarehouseDto;
import com.rustam.modern_dentistry.dto.request.create.WarehouseRemovalCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.WarehouseRemovalProductCreateRequest;
import com.rustam.modern_dentistry.dto.response.create.WarehouseRemovalCreateResponse;
import com.rustam.modern_dentistry.exception.custom.AmountSendException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.util.UtilService;
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
public class WarehouseRemovalService {

    WarehouseRemovalRepository warehouseRemovalRepository;
    UtilService utilService;
    OrderFromWarehouseProductService orderFromWarehouseProductService;

    public void save(WarehouseRemoval warehouseRemoval) {
        warehouseRemovalRepository.save(warehouseRemoval);
    }

    @Transactional
    public WarehouseRemovalCreateResponse create(WarehouseRemovalCreateRequest request) {
        // WarehouseRemoval obyektini tapırıq
        WarehouseRemoval warehouseRemoval = findById(request.getWarehouseRemovalId());

        // Bütün məhsulları işləyirik və DTO-ları hazırlayırıq
        List<OutOfTheWarehouseDto> outOfTheWarehouseDtos = processWarehouseRemovalRequests(request, warehouseRemoval);

        // Cavab qaytarılır
        return WarehouseRemovalCreateResponse.builder()
                .id(warehouseRemoval.getId())
                .date(request.getDate())
                .time(request.getTime())
                .description(request.getDescription())
                .outOfTheWarehouseDtos(outOfTheWarehouseDtos)
                .status(PendingStatus.WAITING)
                .build();
    }

    public WarehouseRemoval findById(Long id) {
        return warehouseRemovalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such warehouse removal found."));
    }

    private List<OutOfTheWarehouseDto> processWarehouseRemovalRequests(WarehouseRemovalCreateRequest request, WarehouseRemoval warehouseRemoval) {
        List<OutOfTheWarehouseDto> dtos = new ArrayList<>();

        for (WarehouseRemovalProductCreateRequest requestDetail : request.getRequests()) {
            // Məhsulu tapırıq və xərcləri yoxlayırıq
            var matchedProduct = findAndValidateProduct(warehouseRemoval, requestDetail);

            // Məhsulu yeniləyirik
            updateWarehouseProductQuantity(matchedProduct, requestDetail);

            // DTO hazırlayırıq və siyahıya əlavə edirik
            dtos.add(createOutOfTheWarehouseDto(matchedProduct, warehouseRemoval, requestDetail));

            // WarehouseRemovalProduct hazırlayırıq və əlavə edirik
            addWarehouseRemovalProduct(warehouseRemoval, matchedProduct, requestDetail);
        }

        return dtos;
    }

    private OrderFromWarehouseProduct findAndValidateProduct(WarehouseRemoval warehouseRemoval, WarehouseRemovalProductCreateRequest requestDetail) {
        // OrderFromWarehouse obyektini tapırıq
        OrderFromWarehouse order = utilService.findById(warehouseRemoval.getOrderFromWarehouse().getId());

        // Məhsulu OrderFromWarehouseProduct ID-yə əsasən tapırıq
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

    private OutOfTheWarehouseDto createOutOfTheWarehouseDto(OrderFromWarehouseProduct matchedProduct, WarehouseRemoval warehouseRemoval, WarehouseRemovalProductCreateRequest requestDetail) {
        return OutOfTheWarehouseDto.builder()
                .categoryName(matchedProduct.getCategoryName())
                .productName(matchedProduct.getProductName())
                .productDescription(matchedProduct.getProductTitle())
                .orderQuantity(warehouseRemoval.getOrderFromWarehouse().getSumQuantity())
                .remainingQuantity(matchedProduct.getQuantity())
                .currentQuantity(requestDetail.getCurrentExpenses())
                .build();
    }

    private void addWarehouseRemovalProduct(WarehouseRemoval warehouseRemoval, OrderFromWarehouseProduct matchedProduct, WarehouseRemovalProductCreateRequest requestDetail) {
        // WarehouseRemovalProduct obyektini hazırlayırıq
        WarehouseRemovalProduct warehouseRemovalProduct = WarehouseRemovalProduct.builder()
                .categoryId(matchedProduct.getCategoryId())
                .productId(matchedProduct.getProductId())
                .sendQuantity(requestDetail.getCurrentExpenses())
                .productName(matchedProduct.getProductName())
                .categoryName(matchedProduct.getCategoryName())
                .productDescription(matchedProduct.getProductTitle())
                .pendingStatus(PendingStatus.WAITING)
                .warehouseRemoval(warehouseRemoval)
                .build();

        // WarehouseRemoval'a əlavə edirik
        warehouseRemoval.getWarehouseRemovalProducts().add(warehouseRemovalProduct);

        // WarehouseRemoval'ın sendAmount və remainingAmount sahələrini yeniləyirik
        long newSendAmount = warehouseRemoval.getSendAmount() + requestDetail.getCurrentExpenses();
        warehouseRemoval.setSendAmount(newSendAmount);

        long newRemainingAmount = warehouseRemoval.getOrderFromWarehouse().getSumQuantity() - newSendAmount;
        warehouseRemoval.setRemainingAmount(newRemainingAmount);
        save(warehouseRemoval);
    }

    @Transactional
    public void deleteWithReturn(Long warehouseRemovalId) {
        // WarehouseRemoval obyektini tapırıq
        WarehouseRemoval warehouseRemoval = findById(warehouseRemovalId);

        // WarehouseRemovalProduct-ları dövr edirik
        for (WarehouseRemovalProduct removalProduct : warehouseRemoval.getWarehouseRemovalProducts()) {
            // Yalnız status `WAITING` olan məhsulları geri qaytarmaq üçün yoxlayırıq
            if (removalProduct.getPendingStatus() == PendingStatus.WAITING) {
                // OrderFromWarehouseProduct obyektini tapırıq
                OrderFromWarehouse orderFromWarehouse = warehouseRemoval.getOrderFromWarehouse();

                var matchedProduct = orderFromWarehouse.getOrderFromWarehouseProducts().stream()
                        .filter(product -> product.getProductId().equals(removalProduct.getProductId()))
                        .findFirst()
                        .orElseThrow(() ->
                                new NotFoundException("Cannot find related OrderFromWarehouseProduct for product ID: " + removalProduct.getProductId()));

                // Məhsul miqdarını geri qaytarırıq
                long updatedQuantity = matchedProduct.getQuantity() + removalProduct.getSendQuantity();
                matchedProduct.setQuantity(updatedQuantity);

                // Məhsulu yeniləyirik (Verilənlər bazasına yazılır)
                orderFromWarehouseProductService.saveOrderFromWarehouseProduct(matchedProduct); // Məhsulu saxlayırıq
            }
        }

        // Əməliyyat bitdikdən sonra WarehouseRemoval obyektini silirik
        warehouseRemovalRepository.delete(warehouseRemoval);
    }
}