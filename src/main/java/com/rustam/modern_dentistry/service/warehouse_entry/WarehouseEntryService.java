package com.rustam.modern_dentistry.service.warehouse_entry;

import com.rustam.modern_dentistry.dao.entity.settings.product.Product;
import com.rustam.modern_dentistry.dao.entity.warehouse_entry.WarehouseEntry;
import com.rustam.modern_dentistry.dao.entity.warehouse_entry.WarehouseEntryProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_entry.WarehouseEntryRepository;
import com.rustam.modern_dentistry.dto.request.create.WarehouseEntryCreateRequest;
import com.rustam.modern_dentistry.dto.response.create.ProductCategoryResponse;
import com.rustam.modern_dentistry.dto.response.create.WarehouseEntryCreateResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.service.settings.product.ProductCategoryService;
import com.rustam.modern_dentistry.service.settings.product.ProductService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class WarehouseEntryService {

    WarehouseEntryRepository warehouseEntryRepository;
    ProductService productService;

    @Transactional
    public WarehouseEntryCreateResponse create(WarehouseEntryCreateRequest warehouseEntryCreateRequest) {
        WarehouseEntry entry = WarehouseEntry.builder()
                .date(warehouseEntryCreateRequest.getDate())
                .time(warehouseEntryCreateRequest.getTime())
                .description(warehouseEntryCreateRequest.getDescription())
                .build();

        List<WarehouseEntryProduct> entryProducts = warehouseEntryCreateRequest.getWarehouseEntryProductCreateRequests().stream()
                .map(productDto -> {
                    Product product = productService.findByIdAndCategoryId(
                            productDto.getProductId(),
                            productDto.getCategoryId()
                    );

                    return WarehouseEntryProduct.builder()
                            .categoryId(productDto.getCategoryId())
                            .productId(productDto.getProductId())
                            .quantity(productDto.getQuantity())
                            .price(productDto.getPrice())
                            .productName(product.getProductName())
                            .categoryName(product.getCategory().getCategoryName())
                            .productTitle(product.getProductTitle())
                            .warehouseEntry(entry)
                            .build();
                })
                .collect(Collectors.toList());

        entry.setWarehouseEntryProducts(entryProducts);
        warehouseEntryRepository.save(entry);

        return WarehouseEntryCreateResponse.builder()
                .date(entry.getDate())
                .time(entry.getTime())
                .description(entry.getDescription())
                .warehouseEntryProducts(entryProducts)
                .build();
    }

}
