package com.rustam.modern_dentistry.mapper.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientInsuranceBalance;
import com.rustam.modern_dentistry.dto.request.create.PatInsuranceBalanceCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatInsuranceBalanceUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatInsuranceBalanceReadResponse;
import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PatientInsuranceBalanceMapper {
    PatientInsuranceBalance toEntity(PatInsuranceBalanceCreateReq request);

    @Mapping(target = "patientInsuranceId", source = "patientInsurance.id")
    PatInsuranceBalanceReadResponse toReadDto(PatientInsuranceBalance entity);

    void update(@MappingTarget PatientInsuranceBalance entity, PatInsuranceBalanceUpdateReq request);
}
