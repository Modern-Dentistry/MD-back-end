package com.rustam.modern_dentistry.util;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlansRepository;
import com.rustam.modern_dentistry.dto.request.create.PatientPlansCreateRequest;
import com.rustam.modern_dentistry.dto.response.create.PatientPlansResponse;
import com.rustam.modern_dentistry.dto.response.read.CategoryOfOperationDto;
import com.rustam.modern_dentistry.dto.response.read.OperationOfCategoryDto;
import com.rustam.modern_dentistry.dto.response.read.PatientPlanPartOfToothDetailDto;
import com.rustam.modern_dentistry.dto.response.read.PatientPlansDto;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientPlanUtilService {

    PatientPlansRepository patientPlansRepository;

    public PatientPlansResponse mapper(PatientPlan savedPlan) {
        return PatientPlansResponse.builder()
                .id(savedPlan.getId())
                .patientPlansDto(PatientPlansDto.builder()
                        .toothNo(savedPlan.getToothId())
                        .operationOfCategoryDto(
                                OperationOfCategoryDto.builder()
                                        .id(savedPlan.getOpType().getId())
                                        .categoryName(savedPlan.getOpType().getCategoryName())
                                        .categoryCode(savedPlan.getOpType().getCategoryCode())
                                        .build()
                        )
                        .partOfTeethIds(savedPlan.getDetails().stream().map(
                                pot -> PatientPlanPartOfToothDetailDto.builder()
                                        .operationName(pot.getOpTypeItem().getOperationName())
                                        .partOfToothId(pot.getPartOfToothId())
                                        .price(pot.getOpTypeItem().getAmount())
                                        .id(pot.getId())
                                        .build()

                        ).toList()).build()).build();
    }

    public PatientPlan findById(UUID id){
        return patientPlansRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such patient plan found."));
    }

    public void validate(UUID patientPlanMainId, Long toothId, Long categoryId, List<Long> partOfTeethIds, Long operationId) {
        if (patientPlansRepository.existsByToothAndOperations(
                patientPlanMainId,toothId,categoryId,partOfTeethIds,operationId
        )){
            throw new ExistsException("gonderilen datalar db-de movcuddur");
        }
    }
}
