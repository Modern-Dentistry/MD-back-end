package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.entity.enums.status.ExecutedStatus;
import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dao.entity.patient_info.patienttreatment.PatientTreatment;
import com.rustam.modern_dentistry.dao.repository.patient_info.PatientTreatmentRepository;
import com.rustam.modern_dentistry.dto.request.create.PatientPlansRequest;
import com.rustam.modern_dentistry.dto.request.create.PatientTreatmentRequest;
import com.rustam.modern_dentistry.util.DateTimeUtil;
import com.rustam.modern_dentistry.util.PatientPlanMainUtilService;
import com.rustam.modern_dentistry.util.PatientPlanUtilService;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientTreatmentCreateService {

    PatientTreatmentRepository patientTreatmentRepository;
    PatientPlanMainUtilService patientPlanMainUtilService;
    PatientPlanUtilService patientPlanUtilService;
    UtilService utilService;

    @Transactional
    public void create(PatientTreatmentRequest request) {
        PatientPlanMain patientPlanMain = patientPlanMainUtilService.findById(request.getPlanMainId());

        List<PatientPlan> selectedPlans = patientPlanUtilService.findAllById(request.getPatientPlansRequests().stream().map(
                PatientPlansRequest::getPlanId
        ).toList());

        Map<UUID, Boolean> planCheckedStatus = request.getPatientPlansRequests().stream()
                .collect(Collectors.toMap(PatientPlansRequest::getPlanId, PatientPlansRequest::getIsChecked));

        selectedPlans.forEach(plan -> {
            plan.setCompleted(planCheckedStatus.get(plan.getId()));
            plan.setUpdatedBy(utilService.getCurrentUserId());
            plan.setUpdatedDate(DateTimeUtil.toEpochMilli(LocalDateTime.now()));
        });

        List<PatientTreatment> treatments = request.getPatientPlansRequests().stream()
                .map(planRequest -> PatientTreatment.builder()
                        .patientPlanMain(patientPlanMain)
                        .executedPlans(List.of(selectedPlans.stream()
                                .filter(plan -> plan.getId().equals(planRequest.getPlanId()))
                                .findFirst()
                                .orElseThrow()))
                        .status("C")
                        .actionStatus("C")
                        .createdDate(DateTimeUtil.toEpochMilli(LocalDateTime.now()))
                        .isChecked(planRequest.getIsChecked())
                        .createdBy(utilService.getCurrentUserId())
                        .executedStatus(ExecutedStatus.FINISHED)
                        .build()).collect(Collectors.toList());
        patientTreatmentRepository.saveAll(treatments);

    }
}