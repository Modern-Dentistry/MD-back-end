package com.rustam.modern_dentistry.service.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.warehouse_operations.DeletionFromWarehouse;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.DeletionFromWarehouseProduct;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseEntryProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_operations.DeletionFromWarehouseRepository;
import com.rustam.modern_dentistry.dto.request.create.DeletionFromWarehouseCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.DeletionFromWarehouseProductRequest;
import com.rustam.modern_dentistry.dto.response.read.DeletionFromWarehouseResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeletionFromWarehouseService {

    DeletionFromWarehouseRepository deletionFromWarehouseRepository;
    WarehouseEntryProductService warehouseEntryProductService;

    @Transactional
    public DeletionFromWarehouseResponse create(DeletionFromWarehouseCreateRequest request) {
        DeletionFromWarehouse deletion = DeletionFromWarehouse.builder()
                .date(request.getDate())
                .time(request.getTime())
                .description(request.getDescription())
                .build();

        List<DeletionFromWarehouseProduct> deletionProducts = new ArrayList<>();

        for (DeletionFromWarehouseProductRequest productRequest : request.getDeletionFromWarehouseProductRequests()) {
            WarehouseEntryProduct entryProduct = warehouseEntryProductService
                    .findAllByIdAndWarehouseEntryIdAndCategoryIdAndProductId(
                            productRequest.getWarehouseEntryProductId(),
                            productRequest.getWarehouseEntryId(),
                            productRequest.getCategoryId(),
                            productRequest.getProductId()
                    );

            if (entryProduct.getQuantity() < productRequest.getQuantity()) {
                throw new NotFoundException("Not enough stock for product: " + entryProduct.getProductName());
            }

            DeletionFromWarehouseProduct deletionProduct = DeletionFromWarehouseProduct.builder()
                    .productId(productRequest.getProductId())
                    .categoryId(productRequest.getCategoryId())
                    .quantity(productRequest.getQuantity())
                    .warehouseEntryId(entryProduct.getWarehouseEntry().getId())
                    .warehouseEntryProductId(entryProduct.getId())
                    .categoryId(entryProduct.getCategoryId())
                    .productId(entryProduct.getProductId())
                    .price(entryProduct.getPrice())
                    .productName(entryProduct.getProductName())
                    .categoryName(entryProduct.getCategoryName())
                    .productTitle(entryProduct.getProductTitle())
                    .deletionFromWarehouse(deletion)
                    .build();

            deletionProducts.add(deletionProduct);

            if (entryProduct.getQuantity().equals(productRequest.getQuantity())) {
                warehouseEntryProductService.delete(entryProduct);
            }
            else {
                entryProduct.setQuantity(entryProduct.getQuantity() - productRequest.getQuantity());
            }
        }

        deletion.setDeletionFromWarehouseProducts(deletionProducts);
        deletion.setNumber(deletionProducts.size());
        deletionFromWarehouseRepository.save(deletion);

        return DeletionFromWarehouseResponse.builder()
                .id(deletion.getId())
                .date(deletion.getDate())
                .time(deletion.getTime())
                .number(deletionProducts.size())
                .build();
    }

}
