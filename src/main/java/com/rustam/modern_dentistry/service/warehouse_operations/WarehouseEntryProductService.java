package com.rustam.modern_dentistry.service.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseEntryProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_operations.WarehouseEntryProductRepository;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;

import com.rustam.modern_dentistry.exception.custom.ProductDoesnotQuantityThatMuchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseEntryProductService {

    private final WarehouseEntryProductRepository warehouseEntryProductRepository;

    public void decreaseProductQuantity(Long productId, long quantityToDecrease) {
        List<WarehouseEntryProduct> products = getProductsByProductId(productId);
        long totalQuantity = calculateTotalQuantity(products);

        if (totalQuantity < quantityToDecrease) {
            throw new ProductDoesnotQuantityThatMuchException("Anbarda kifayət qədər məhsul yoxdur");
        }

        updateProductQuantities(products, quantityToDecrease);
    }

    private List<WarehouseEntryProduct> getProductsByProductId(Long productId) {
        List<WarehouseEntryProduct> products = warehouseEntryProductRepository.findAllByProductId(productId);
        if (products.isEmpty()) {
            throw new NotFoundException("Anbarda bu məhsul tapılmadı");
        }
        return products;
    }

    public long calculateTotalQuantity(List<WarehouseEntryProduct> products) {
        return products.stream()
                .mapToLong(WarehouseEntryProduct::getQuantity)
                .sum();
    }

    private void updateProductQuantities(List<WarehouseEntryProduct> products, long quantityToDecrease) {
        long remaining = quantityToDecrease;

        for (WarehouseEntryProduct product : products) {
            if (remaining == 0) break;

            long current = product.getQuantity();
            long deducted = Math.min(current, remaining);

            product.setQuantity(current - deducted);
            remaining -= deducted;

            warehouseEntryProductRepository.save(product);
        }
    }

    public void increaseProductQuantity(Long productId, long quantityToIncrease) {
        List<WarehouseEntryProduct> products = warehouseEntryProductRepository.findAllByProductId(productId);

        if (products.isEmpty()) {
            throw new NotFoundException("Bu məhsul anbarda tapılmadı");
        }

        WarehouseEntryProduct product = products.get(0);
        product.setQuantity(product.getQuantity() + quantityToIncrease);
        warehouseEntryProductRepository.save(product);
    }

}
