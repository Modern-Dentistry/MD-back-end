package com.rustam.modern_dentistry.service.warehouse_entry;

import com.rustam.modern_dentistry.dao.entity.settings.product.Product;
import com.rustam.modern_dentistry.dao.entity.warehouse_entry.WarehouseEntry;
import com.rustam.modern_dentistry.dao.entity.warehouse_entry.WarehouseEntryProduct;
import com.rustam.modern_dentistry.dao.repository.warehouse_entry.WarehouseEntryRepository;
import com.rustam.modern_dentistry.dto.request.create.WarehouseEntryCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.WarehouseEntrySearchRequest;
import com.rustam.modern_dentistry.dto.request.update.WarehouseEntryUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.ProductCategoryResponse;
import com.rustam.modern_dentistry.dto.response.create.WarehouseEntryCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.WarehouseEntryReadResponse;
import com.rustam.modern_dentistry.dto.response.read.WarehouseEntryResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.exception.custom.WarehouseEntryNotFoundException;
import com.rustam.modern_dentistry.mapper.warehouse_operations.WarehouseEntryMapper;
import com.rustam.modern_dentistry.service.settings.product.ProductCategoryService;
import com.rustam.modern_dentistry.service.settings.product.ProductService;
import com.rustam.modern_dentistry.util.specification.warehouse_entry.WarehouseEntrySpecification;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class WarehouseEntryService {

    WarehouseEntryRepository warehouseEntryRepository;
    WarehouseEntryMapper warehouseEntryMapper;
    ProductService productService;

    @Transactional
    public WarehouseEntryCreateResponse create(WarehouseEntryCreateRequest warehouseEntryCreateRequest) {
        WarehouseEntry entry = WarehouseEntry.builder()
                .date(warehouseEntryCreateRequest.getDate())
                .time(warehouseEntryCreateRequest.getTime())
                .description(warehouseEntryCreateRequest.getDescription())
                .build();

        BigDecimal sumPrice = warehouseEntryCreateRequest.getWarehouseEntryProductCreateRequests()
                .stream()
                .map(productDto -> productDto.getPrice().multiply(BigDecimal.valueOf(productDto.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        entry.setSumPrice(sumPrice);

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

        entry.setNumber(entryProducts.size());
        entry.setWarehouseEntryProducts(entryProducts);
        warehouseEntryRepository.save(entry);

        return WarehouseEntryCreateResponse.builder()
                .date(entry.getDate())
                .time(entry.getTime())
                .description(entry.getDescription())
                .warehouseEntryProducts(entryProducts)
                .build();
    }

    public List<WarehouseEntryReadResponse> read() {
        List<WarehouseEntry> entries = warehouseEntryRepository.findAll();

        return entries.stream()
                .map(entry -> WarehouseEntryReadResponse.builder()
                        .id(entry.getId())
                        .localDateTime(LocalDateTime.of(entry.getDate(), entry.getTime()))
                        .number(entry.getNumber())
                        .sumPrice(entry.getSumPrice())
                        .build())
                .toList();
    }

    public List<WarehouseEntryReadResponse> search(WarehouseEntrySearchRequest warehouseEntrySearchRequest) {
        List<WarehouseEntry> warehouseEntries = warehouseEntryRepository.findAll(WarehouseEntrySpecification.filterBy(warehouseEntrySearchRequest));
        return warehouseEntries.stream()
                .map(entry -> WarehouseEntryReadResponse.builder()
                        .id(entry.getId())
                        .localDateTime(LocalDateTime.of(entry.getDate(), entry.getTime()))
                        .number(entry.getNumber())
                        .sumPrice(entry.getSumPrice())
                        .build())
                .toList();
    }

    public WarehouseEntry findById(Long id){
        return warehouseEntryRepository.findById(id)
                .orElseThrow(() -> new WarehouseEntryNotFoundException("No such warehouse access operation found."));
    }

    public WarehouseEntryResponse info(Long id) {
        WarehouseEntry warehouseEntry = findById(id);
        return warehouseEntryMapper.toDto(warehouseEntry);
    }


    @Transactional
    public WarehouseEntryCreateResponse update(WarehouseEntryUpdateRequest request) {
        WarehouseEntry entry = findById(request.getId());

        updateFieldIfPresent(request.getDate(), entry::setDate);
        updateFieldIfPresent(request.getTime(), entry::setTime);
        updateFieldIfPresent(request.getDescription(), entry::setDescription);

        if (request.getWarehouseEntryProductCreateRequests() != null && !request.getWarehouseEntryProductCreateRequests().isEmpty()) {
            List<WarehouseEntryProduct> entryProducts = mapToWarehouseEntryProducts(request, entry);

            BigDecimal sumPrice = entryProducts.stream()
                    .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            entry.setWarehouseEntryProducts(entryProducts);
            entry.setNumber(entryProducts.size());
            entry.setSumPrice(sumPrice);
        }

        warehouseEntryRepository.save(entry);

        return WarehouseEntryCreateResponse.builder()
                .date(entry.getDate())
                .time(entry.getTime())
                .description(entry.getDescription())
                .warehouseEntryProducts(entry.getWarehouseEntryProducts())
                .build();
    }

    public void delete(Long id) {
        WarehouseEntry warehouseEntry = findById(id);
        warehouseEntryRepository.delete(warehouseEntry);
    }

    private <T> void updateFieldIfPresent(T newValue, Consumer<T> setter) {
        if (newValue != null && !(newValue instanceof String str && str.isBlank())) {
            setter.accept(newValue);
        }
    }

    private List<WarehouseEntryProduct> mapToWarehouseEntryProducts(WarehouseEntryUpdateRequest request, WarehouseEntry entry) {
        return request.getWarehouseEntryProductCreateRequests().stream()
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
                .toList();
    }

}
