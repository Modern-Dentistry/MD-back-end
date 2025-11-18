package com.rustam.modern_dentistry.mapper.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dto.response.read.PatientPlansMainResponse;
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
public interface PatientPlansMainMapper {
    PatientPlansMainResponse toDto(PatientPlanMain save);

    List<PatientPlansMainResponse> toDtos(List<PatientPlanMain> all);

    PatientPlanMain toReadById(PatientPlanMain patientPlanMain);
}
