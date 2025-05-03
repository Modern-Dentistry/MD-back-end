package com.rustam.modern_dentistry.mapper;

import com.rustam.modern_dentistry.dao.entity.PatientBlacklist;
import com.rustam.modern_dentistry.dao.entity.settings.BlacklistResult;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dto.request.update.PatBlacklistUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatBlacklistReadRes;
import org.mapstruct.*;

import java.time.LocalDate;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PatientBlacklistMapper {
    @Mapping(target = "id", ignore = true)
    PatientBlacklist toEntity(BlacklistResult blacklistResult, Patient patient);

    void update(@MappingTarget PatientBlacklist patientBlacklist, PatBlacklistUpdateReq request);

    default PatBlacklistReadRes toReadDto(PatientBlacklist patientBlacklist) {
        return PatBlacklistReadRes.builder()
                .fullName(patientBlacklist.getPatient().getName() + patientBlacklist.getPatient().getSurname())
                .finCode(patientBlacklist.getPatient().getFinCode())
                .mobilePhone(patientBlacklist.getPatient().getPhone())
                .addedDate(LocalDate.now())
                .blacklistReason(patientBlacklist.getBlacklistResult().getStatusName())
                .build();
    }
}
