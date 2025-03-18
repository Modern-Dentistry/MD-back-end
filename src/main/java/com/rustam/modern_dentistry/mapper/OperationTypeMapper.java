package com.rustam.modern_dentistry.mapper;

import com.rustam.modern_dentistry.dao.entity.OperationType;
import com.rustam.modern_dentistry.dto.request.create.OperationTypeCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.OperationTypeUpdateRequest;
import com.rustam.modern_dentistry.dto.response.excel.OperationTypeExcelResponse;
import com.rustam.modern_dentistry.dto.response.read.OperationTypeReadResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OperationTypeMapper {
    OperationTypeMapper OPERATION_TYPE_MAPPER = Mappers.getMapper(OperationTypeMapper.class);

    @Mapping(target = "status", defaultValue = "ACTIVE")
    OperationType toEntity(OperationTypeCreateRequest request);

    OperationTypeReadResponse toReadDto(OperationType entity);

    OperationTypeExcelResponse toExcelDto(OperationType entity);

    void updateOperationType(@MappingTarget OperationType entity, OperationTypeUpdateRequest request);

}
