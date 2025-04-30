package com.rustam.modern_dentistry.dto.response.create;

import com.rustam.modern_dentistry.dao.entity.enums.status.PendingStatus;
import com.rustam.modern_dentistry.dto.OutOfTheWarehouseDto;
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
public class WarehouseRemovalCreateResponse {

    Long id;

    LocalDate date;

    LocalTime time;

    List<OutOfTheWarehouseDto> outOfTheWarehouseDtos;

    String description;

    PendingStatus status;
}
