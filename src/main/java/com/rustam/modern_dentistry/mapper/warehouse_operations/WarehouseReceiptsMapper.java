package com.rustam.modern_dentistry.mapper.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseReceipts;
import com.rustam.modern_dentistry.dto.response.read.WarehouseReceiptsResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface WarehouseReceiptsMapper {
    @Mapping(target = "cabinetName", source = "cabinet.cabinetName")
    List<WarehouseReceiptsResponse> toDtos(List<WarehouseReceipts> warehouseReceipts);

    @Mapping(target = "cabinetName", source = "cabinet.cabinetName")
    WarehouseReceiptsResponse toDto(WarehouseReceipts warehouseReceipts);
}
