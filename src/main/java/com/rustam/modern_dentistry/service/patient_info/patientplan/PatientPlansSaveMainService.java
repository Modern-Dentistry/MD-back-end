package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlanMainRepository;
import com.rustam.modern_dentistry.dto.request.save.PatientPlansSaveRequest;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.util.DateTimeUtil;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientPlansSaveMainService {

    PatientPlanMainRepository patientPlanMainRepository;
    UtilService utilService;

    @Transactional
    public void save(UUID id) {
        PatientPlanMain patientPlanMain = patientPlanMainRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("No such patient plan main found."));
        savedData(patientPlanMain);
    }

    private void savedData(PatientPlanMain patientPlanMain) {
        patientPlanMain.setActionStatus("A");
        patientPlanMain.setStatus("A");
        patientPlanMain.setUpdatedBy(utilService.getCurrentUserId());
        patientPlanMain.setUpdatedDate(DateTimeUtil.toEpochMilli(LocalDateTime.now()));

        patientPlanMain.getPatientPlans().forEach(plan -> {
            plan.setActionStatus("A");
            plan.setStatus("A");
            plan.setUpdatedBy(utilService.getCurrentUserId());
            plan.setUpdatedDate(DateTimeUtil.toEpochMilli(LocalDateTime.now()));
        });
        patientPlanMainRepository.save(patientPlanMain);
    }
}