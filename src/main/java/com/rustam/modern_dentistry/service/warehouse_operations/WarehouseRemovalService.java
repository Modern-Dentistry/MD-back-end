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

    public WarehouseRemoval findById(Long id) {
        return warehouseRemovalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such warehouse removal found."));
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
                long updatedQuantity = matchedProduct.getQuantity() + removalProduct.getCurrentAmount();
                matchedProduct.setQuantity(updatedQuantity);

                // Məhsulu yeniləyirik (Verilənlər bazasına yazılır)
                orderFromWarehouseProductService.saveOrderFromWarehouseProduct(matchedProduct); // Məhsulu saxlayırıq
            }
        }

        // Əməliyyat bitdikdən sonra WarehouseRemoval obyektini silirik
        warehouseRemovalRepository.delete(warehouseRemoval);
    }

}