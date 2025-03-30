package com.rustam.modern_dentistry.mapper.settings.anemnesis;

import com.rustam.modern_dentistry.dao.entity.settings.anamnesis.AnamnesisCategory;
import com.rustam.modern_dentistry.dto.request.create.AnemnesisCatCreateReq;
import com.rustam.modern_dentistry.dto.request.update.UpdateAnemnesisCatReq;
import com.rustam.modern_dentistry.dto.response.read.AnemnesisCatReadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AnemnesisCategoryMapper {
    AnemnesisCategoryMapper ANAMNESIS_CAT_MAPPER = Mappers.getMapper(AnemnesisCategoryMapper.class);

    AnamnesisCategory toEntity(AnemnesisCatCreateReq request);

    AnemnesisCatReadResponse toReadDto(AnamnesisCategory entity);

    void updateAnemnesisCategory(@MappingTarget AnamnesisCategory entity, UpdateAnemnesisCatReq request);
}

