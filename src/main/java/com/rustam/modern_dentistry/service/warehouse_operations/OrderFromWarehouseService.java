package com.rustam.modern_dentistry.service.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.settings.product.Product;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouse;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouseProduct;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseEntryProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_operations.OrderFromWarehouseRepository;
import com.rustam.modern_dentistry.dto.request.create.OrderFromWarehouseCreateRequest;
import com.rustam.modern_dentistry.dto.response.create.WarehouseEntryCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.OrderFromWarehouseProductResponse;
import com.rustam.modern_dentistry.dto.response.read.OrderFromWarehouseResponse;
import com.rustam.modern_dentistry.dto.response.read.WarehouseEntryProductResponse;
import com.rustam.modern_dentistry.service.settings.product.ProductService;
import com.rustam.modern_dentistry.util.UtilService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderFromWarehouseService {

    OrderFromWarehouseRepository orderFromWarehouseRepository;
    ProductService productService;
    UtilService utilService;
    WarehouseEntryProductService warehouseEntryProductService;

    @Transactional
    public OrderFromWarehouseResponse create(OrderFromWarehouseCreateRequest request) {
        String personWhoPlacedOrder = utilService.getCurrentUserId();
        OrderFromWarehouse orderFromWarehouse = OrderFromWarehouse.builder()
                .date(request.getDate())
                .time(request.getTime())
                .description(request.getDescription())
                .room(request.getRoom())
                .personWhoPlacedOrder(personWhoPlacedOrder)
                .build();

        List<OrderFromWarehouseProduct> orderFromWarehouseProducts = buildOrderFromWarehouseProducts(request,orderFromWarehouse);
        orderFromWarehouse.setNumber(orderFromWarehouseProducts.size());
        orderFromWarehouse.setOrderFromWarehouseProducts(orderFromWarehouseProducts);

        orderFromWarehouseRepository.save(orderFromWarehouse);
        return buildWarehouseEntryResponse(orderFromWarehouse, orderFromWarehouseProducts);
    }

    private OrderFromWarehouseResponse buildWarehouseEntryResponse(OrderFromWarehouse orderFromWarehouse, List<OrderFromWarehouseProduct> orderFromWarehouseProducts) {
        List<OrderFromWarehouseProductResponse> productResponses = orderFromWarehouseProducts.stream()
                .map(p -> OrderFromWarehouseProductResponse.builder()
                        .categoryName(p.getCategoryName())
                        .productName(p.getProductName())
                        .productTitle(p.getProductTitle())
                        .quantity(p.getQuantity())
                        .build())
                .collect(Collectors.toList());

        return OrderFromWarehouseResponse.builder()
                .date(orderFromWarehouse.getDate())
                .time(orderFromWarehouse.getTime())
                .description(orderFromWarehouse.getDescription())
                .orderFromWarehouseProductResponses(productResponses)
                .room(orderFromWarehouse.getRoom())
                .build();
    }

    private List<OrderFromWarehouseProduct> buildOrderFromWarehouseProducts(OrderFromWarehouseCreateRequest request, OrderFromWarehouse orderFromWarehouse) {
        return request.getOrderFromWarehouseProductRequests().stream()
                .map(dto -> {
                    Product product = productService.findByIdAndCategoryId(dto.getProductId(), dto.getCategoryId());
                    warehouseEntryProductService.decreaseProductQuantity(dto.getProductId(),dto.getQuantity());
                    return OrderFromWarehouseProduct.builder()
                            .categoryId(dto.getCategoryId())
                            .productId(dto.getProductId())
                            .quantity(dto.getQuantity())
                            .productName(product.getProductName())
                            .categoryName(product.getCategory().getCategoryName())
                            .productTitle(product.getProductTitle())
                            .orderFromWarehouse(orderFromWarehouse)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
