package com.rustam.modern_dentistry.mapper;

import com.rustam.modern_dentistry.dao.entity.PatientBlacklist;
import com.rustam.modern_dentistry.dto.request.create.PatBlacklistCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatBlacklistUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatBlacklistReadRes;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PatientBlacklistMapper {
    PatientBlacklist toEntity(PatBlacklistCreateReq request);

    PatBlacklistReadRes toReadDto(PatientBlacklist entity);

    void update(@MappingTarget PatientBlacklist patientBlacklist, PatBlacklistUpdateReq request);
}
