package com.rustam.modern_dentistry.mapper.settings.teeth;

import com.rustam.modern_dentistry.dao.entity.settings.teeth.TeethOperation;
import com.rustam.modern_dentistry.dto.response.read.TeethOperationResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TeethOperationMapper {
    @Mapping(target = "operationName", source = "opTypeItem.operationName")
    TeethOperationResponse toDto(TeethOperation byId);
}
