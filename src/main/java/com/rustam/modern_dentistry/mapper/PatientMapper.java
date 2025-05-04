package com.rustam.modern_dentistry.mapper;

import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dto.response.read.PatientReadResponse;
import com.rustam.modern_dentistry.dto.response.update.PatientUpdateResponse;
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
public interface PatientMapper {
    PatientUpdateResponse toUpdatePatient(Patient patient);

    List<PatientReadResponse> toDtos(List<Patient> users);

    @Mapping(target = "isBlocked", expression = "java(patient.getPatientBlacklist() != null)")
    PatientReadResponse toRead(Patient patient);
}
