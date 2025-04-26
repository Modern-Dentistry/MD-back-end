package com.rustam.modern_dentistry.service.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.settings.product.Product;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouse;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouseProduct;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseEntry;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseEntryProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_operations.OrderFromWarehouseRepository;
import com.rustam.modern_dentistry.dto.request.create.OrderFromWarehouseCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.OrderFromWarehouseProductRequest;
import com.rustam.modern_dentistry.dto.request.read.OrderFromWarehouseSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.OrderFromWarehouseProductUpdateRequest;
import com.rustam.modern_dentistry.dto.request.update.OrderFromWarehouseUpdateRequest;
import com.rustam.modern_dentistry.dto.request.update.WarehouseEntryProductUpdateRequest;
import com.rustam.modern_dentistry.dto.request.update.WarehouseEntryUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.WarehouseEntryCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.OrderFromWarehouseProductResponse;
import com.rustam.modern_dentistry.dto.response.read.OrderFromWarehouseReadResponse;
import com.rustam.modern_dentistry.dto.response.read.OrderFromWarehouseResponse;
import com.rustam.modern_dentistry.dto.response.read.WarehouseEntryProductResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.exception.custom.ProductDoesnotQuantityThatMuchException;
import com.rustam.modern_dentistry.mapper.warehouse_operations.OrderFromWarehouseMapper;
import com.rustam.modern_dentistry.service.settings.product.ProductService;
import com.rustam.modern_dentistry.util.UtilService;
import com.rustam.modern_dentistry.util.specification.warehouse_entry.OrderFromWarehouseSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class OrderFromWarehouseService {

    OrderFromWarehouseRepository orderFromWarehouseRepository;
    ProductService productService;
    UtilService utilService;
    WarehouseEntryProductService warehouseEntryProductService;
    OrderFromWarehouseMapper orderFromWarehouseMapper;

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
        orderFromWarehouse.setSumQuantity(calculateSumQuantity(orderFromWarehouseProducts));

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
                .personWhoPlacedOrder(orderFromWarehouse.getPersonWhoPlacedOrder())
                .number(orderFromWarehouse.getNumber())
                .quantity(orderFromWarehouse.getSumQuantity())
                .build();
    }

    private Long calculateSumQuantity(List<OrderFromWarehouseProduct> products) {
        return products.stream()
                .mapToLong(OrderFromWarehouseProduct::getQuantity)
                .sum();
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

    public List<OrderFromWarehouseReadResponse> read() {
        List<OrderFromWarehouse> entries = orderFromWarehouseRepository.findAll();
        return orderFromWarehouseMapper.toDtos(entries);
    }

    @Transactional
    public OrderFromWarehouseResponse info(Long id) {
        OrderFromWarehouse orderFromWarehouse = findById(id);
        return buildWarehouseEntryResponse(orderFromWarehouse, orderFromWarehouse.getOrderFromWarehouseProducts());
    }

    public OrderFromWarehouse findById(Long id){
        return orderFromWarehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such order from warehouse found."));
    }

//    public OrderFromWarehouse findByOrderFromWarehouseProductId(Long id){
//        return orderFromWarehouseRepository.findByIdWithProducts(id)
//                .orElseThrow(() -> new RuntimeException("OrderFromWarehouse not found"));
//    }

    public List<OrderFromWarehouseReadResponse> search(OrderFromWarehouseSearchRequest orderFromWarehouseSearchRequest) {
        List<OrderFromWarehouse> orderFromWarehouses = orderFromWarehouseRepository.findAll(OrderFromWarehouseSpecification.filterBy(orderFromWarehouseSearchRequest));
        return orderFromWarehouseMapper.toDtos(orderFromWarehouses);
    }

    @Transactional
    public OrderFromWarehouseResponse update(OrderFromWarehouseUpdateRequest orderFromWarehouseUpdateRequest) {
        OrderFromWarehouse orderFromWarehouse = findById(orderFromWarehouseUpdateRequest.getId());
        utilService.updateFieldIfPresent(orderFromWarehouseUpdateRequest.getDate(), orderFromWarehouse::setDate);
        utilService.updateFieldIfPresent(orderFromWarehouseUpdateRequest.getTime(), orderFromWarehouse::setTime);
        utilService.updateFieldIfPresent(orderFromWarehouseUpdateRequest.getDescription(), orderFromWarehouse::setDescription);
        utilService.updateFieldIfPresent(orderFromWarehouseUpdateRequest.getRoom(), orderFromWarehouse::setRoom);

        if (hasProducts(orderFromWarehouseUpdateRequest)){
            List<OrderFromWarehouseProduct> entryProducts = buildWarehouseEntryUpdateProducts(orderFromWarehouseUpdateRequest, orderFromWarehouse);
            orderFromWarehouse.getOrderFromWarehouseProducts().clear();
            orderFromWarehouse.getOrderFromWarehouseProducts().addAll(entryProducts);
            orderFromWarehouse.setNumber(entryProducts.size());
            orderFromWarehouse.setSumQuantity(calculateSumQuantity(entryProducts));
        }

        orderFromWarehouseRepository.save(orderFromWarehouse);
        return buildWarehouseEntryResponse(orderFromWarehouse, orderFromWarehouse.getOrderFromWarehouseProducts());
    }

    private List<OrderFromWarehouseProduct> buildWarehouseEntryUpdateProducts(OrderFromWarehouseUpdateRequest orderFromWarehouseUpdateRequest, OrderFromWarehouse orderFromWarehouse) {
        Map<Long, OrderFromWarehouseProduct> existingProductsMap = orderFromWarehouse.getOrderFromWarehouseProducts()
                .stream()
                .collect(Collectors.toMap(OrderFromWarehouseProduct::getId, Function.identity()));

        List<OrderFromWarehouseProduct> finalList = new ArrayList<>();

        Set<Long> updatedIds = new HashSet<>();

        for (OrderFromWarehouseProductUpdateRequest dto : orderFromWarehouseUpdateRequest.getOrderFromWarehouseProductUpdateRequests()) {
            Product product = productService.findByIdAndCategoryId(dto.getProductId(), dto.getCategoryId());

            if (dto.getOrderFromWarehouseProductId() != null && existingProductsMap.containsKey(dto.getOrderFromWarehouseProductId())) {
                OrderFromWarehouseProduct existing = existingProductsMap.get(dto.getOrderFromWarehouseProductId());

                warehouseEntryProductService.increaseProductQuantity(dto.getProductId(), existing.getQuantity());
                warehouseEntryProductService.decreaseProductQuantity(dto.getProductId(), dto.getQuantity());

                utilService.updateFieldIfPresent(dto.getCategoryId(), existing::setCategoryId);
                utilService.updateFieldIfPresent(dto.getProductId(), existing::setProductId);
                utilService.updateFieldIfPresent(dto.getQuantity(), existing::setQuantity);

                existing.setProductName(product.getProductName());
                existing.setCategoryName(product.getCategory().getCategoryName());
                existing.setProductTitle(product.getProductTitle());

                finalList.add(existing);
                updatedIds.add(dto.getOrderFromWarehouseProductId());

            } else {
                if (dto.getOrderFromWarehouseProductId() != null && !existingProductsMap.containsKey(dto.getOrderFromWarehouseProductId())) {
                    throw new ProductDoesnotQuantityThatMuchException("OrderFromWarehouseProduct with id " + dto.getOrderFromWarehouseProductId() + " not found in this OrderFromWarehouse!");
                }

                warehouseEntryProductService.decreaseProductQuantity(dto.getProductId(), dto.getQuantity());

                OrderFromWarehouseProduct newProduct = OrderFromWarehouseProduct.builder()
                        .categoryId(dto.getCategoryId())
                        .productId(dto.getProductId())
                        .quantity(dto.getQuantity())
                        .productName(product.getProductName())
                        .categoryName(product.getCategory().getCategoryName())
                        .productTitle(product.getProductTitle())
                        .orderFromWarehouse(orderFromWarehouse)
                        .build();

                finalList.add(newProduct);
            }

        }


        for (OrderFromWarehouseProduct old : existingProductsMap.values()) {
            if (!updatedIds.contains(old.getId())) {
                finalList.add(old);
            }
        }

        return finalList;
    }

    private boolean hasProducts(OrderFromWarehouseUpdateRequest request) {
        return request.getOrderFromWarehouseProductUpdateRequests() != null &&
                !request.getOrderFromWarehouseProductUpdateRequests().isEmpty();
    }

    public void delete(Long id) {
        OrderFromWarehouse orderFromWarehouse = findById(id);
        orderFromWarehouseRepository.delete(orderFromWarehouse);
    }


    @Transactional
    public void deleteOrderFromWarehouseProduct(Long id, Long productId) {
        OrderFromWarehouse orderFromWarehouse = findById(id);

        orderFromWarehouse.getOrderFromWarehouseProducts().removeIf(p -> p.getId().equals(productId));

        orderFromWarehouse.setNumber(orderFromWarehouse.getOrderFromWarehouseProducts().size());

        orderFromWarehouseRepository.save(orderFromWarehouse);
    }
}
