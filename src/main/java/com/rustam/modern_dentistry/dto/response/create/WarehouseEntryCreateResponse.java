package com.rustam.modern_dentistry.dto.response.create;

import com.rustam.modern_dentistry.dao.entity.warehouse_entry.WarehouseEntryProduct;
import com.rustam.modern_dentistry.dto.response.read.WarehouseEntryProductResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseEntryCreateResponse {

    LocalDate date;

    LocalTime time;

    List<WarehouseEntryProductResponse> warehouseEntryProducts;

    String description;
}
