package com.rustam.modern_dentistry.dto.request.read;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderFromWarehouseProductRequest {

    Long warehouseEntryId;
    Long categoryId;
    Long productId;
    Long quantity;
}
