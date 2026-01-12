package com.rustam.modern_dentistry.util;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlansRepository;
import com.rustam.modern_dentistry.dto.request.create.PatientPlansCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.PatientTreatmentRequest;
import com.rustam.modern_dentistry.dto.response.create.PatientPlansResponse;
import com.rustam.modern_dentistry.dto.response.read.*;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    public PatientPlan findById(UUID id) {
        return patientPlansRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such patient plan found."));
    }

    public void validate(UUID patientPlanMainId, Long toothId, Long categoryId, List<Long> partOfTeethIds, Long operationId) {
        if (patientPlansRepository.existsByToothAndOperations(
                patientPlanMainId, toothId, categoryId, partOfTeethIds, operationId
        )) {
            throw new ExistsException("gonderilen datalar db-de movcuddur");
        }
    }

    public PatientPlanMain existsByDateOfPatientPlanMain(UUID planMainId) {
        return patientPlansRepository.findByDateOfPatientPlanMain(planMainId)
                .orElseThrow(() -> new NotFoundException("PatientPlanMain not found with given criteria"));
    }

    @Transactional(readOnly = true)
    public ReadByPatientPlanMainIdOfTreatment read(UUID planMainId) {
        PatientPlanMain planMain = existsByDateOfPatientPlanMain(planMainId);

        if (planMain == null || planMain.getPatientPlans() == null) {
            return null;
        }

        List<PlanDetailDto> planDetails = planMain.getPatientPlans().stream()
                .filter(plan -> "A".equals(plan.getStatus()) && "A".equals(plan.getActionStatus()))
                .map(plan -> PlanDetailDto.builder()
                        .patientPlanId(plan.getId())
                        .categoryId(plan.getOpType().getId())
                        .categoryName(plan.getOpType().getCategoryName())
                        .categoryCode(plan.getOpType().getCategoryCode())
                        .toothNo(plan.getToothId())
                        .details(plan.getDetails().stream()
                                .map(detail -> PatientPlansOfTreatmentResponse.builder()
                                        .id(detail.getId())
                                        .partOfToothId(detail.getPartOfToothId())
                                        .operationName(detail.getOpTypeItem().getOperationName())
                                        .operationCode(detail.getOpTypeItem().getOperationCode())
                                        .price(detail.getOpTypeItem().getAmount())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());

        return ReadByPatientPlanMainIdOfTreatment.builder()
                .key(planMain.getKey())
                .patientPlanMainId(planMain.getId())
                .isSave("A".equals(planMain.getActionStatus()))
                .plans(planDetails)
                .build();
    }

    public List<PatientPlan> findAllById(List<UUID> list) {
        return patientPlansRepository.findAllById(list);
    }

    public PatientPlan existsByPlanMainIdAndToothIdAndCategoryIdAndOperationId(UUID patientPlanMainId, Long toothId, Long categoryId, Long operationId) {
        return patientPlansRepository.findByPatientPlanMainIdAndToothIdAndOpTypeItemAndCategoryId(patientPlanMainId, toothId , operationId, categoryId).orElse(null);
    }
}
