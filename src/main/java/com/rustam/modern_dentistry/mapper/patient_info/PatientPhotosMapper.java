package com.rustam.modern_dentistry.mapper.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientPhotos;
import com.rustam.modern_dentistry.dto.request.create.PatPhotosCreateReq;
import com.rustam.modern_dentistry.dto.response.read.PatPhotosReadRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PatientPhotosMapper {

    PatientPhotos toEntity(PatPhotosCreateReq request, String fileName);

    @Mapping(target = "patientId", source = "patient.id")
    PatPhotosReadRes toResponse(PatientPhotos entity, String url);
}
