package com.rustam.modern_dentistry.mapper.settings;

import com.rustam.modern_dentistry.dao.entity.settings.SpecializationCategory;
import com.rustam.modern_dentistry.dto.response.read.SpecializationCategoryResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SpecializationCategoryMapper {

    SpecializationCategoryResponse toDto(SpecializationCategory save);

    List<SpecializationCategoryResponse> toDtos(List<SpecializationCategory> all);
}
