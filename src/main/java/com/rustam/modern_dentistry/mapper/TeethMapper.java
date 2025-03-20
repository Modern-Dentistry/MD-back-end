package com.rustam.modern_dentistry.mapper;

import com.rustam.modern_dentistry.dao.entity.teeth.Teeth;
import com.rustam.modern_dentistry.dto.response.read.TeethResponse;
import com.rustam.modern_dentistry.dto.response.update.TeethUpdateResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TeethMapper {
    TeethMapper INSTANCE = Mappers.getMapper(TeethMapper.class);
    TeethResponse toTeethResponse(Teeth teeth);

    TeethUpdateResponse toUpdateResponse(Teeth teeth);
}
