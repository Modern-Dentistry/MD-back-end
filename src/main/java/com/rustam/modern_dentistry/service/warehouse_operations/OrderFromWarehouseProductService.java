package com.rustam.modern_dentistry.service.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouseProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_operations.OrderFromWarehouseProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderFromWarehouseProductService {

    OrderFromWarehouseProductRepository orderFromWarehouseProductRepository;

    public void saveOrderFromWarehouseProduct(OrderFromWarehouseProduct matchedProduct) {
        orderFromWarehouseProductRepository.save(matchedProduct);
    }
}
