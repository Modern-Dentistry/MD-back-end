package com.rustam.modern_dentistry.mapper;

import com.rustam.modern_dentistry.dao.entity.InsuranceCompany;
import com.rustam.modern_dentistry.dto.request.create.InsuranceCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.UpdateICRequest;
import com.rustam.modern_dentistry.dto.response.read.InsuranceReadResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InsuranceCompanyMapper {
    InsuranceCompanyMapper INSURANCE_COMPANY_MAPPER = Mappers.getMapper(InsuranceCompanyMapper.class);

    @Mapping(target = "status", expression = "java(com.rustam.modern_dentistry.dao.entity.enums.status.Status.ACTIVE)")
    InsuranceCompany toEntity(InsuranceCreateRequest request);

    InsuranceReadResponse toReadDto(InsuranceCompany entity);

    void updateInsuranceCompany(@MappingTarget InsuranceCompany product, UpdateICRequest request);
}
