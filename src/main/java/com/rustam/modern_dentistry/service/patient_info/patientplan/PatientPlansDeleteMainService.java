package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlanMainRepository;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.util.DateTimeUtil;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientPlansDeleteMainService {

    PatientPlanMainRepository patientPlanMainRepository;
    UtilService utilService;

    public void delete(UUID id) {
        PatientPlanMain patientPlanMain = patientPlanMainRepository.findByIdAndStatusInAndActionStatusIn(id, List.of("A", "C"), List.of("A", "C"))
                .orElseThrow(() -> new NotFoundException("No such patient plan main found with id: " + id + " or it is deleted."));
        String responsiblePerson = utilService.getCurrentUserId();
        Long l = DateTimeUtil.toEpochMilli(LocalDateTime.now());
        patientPlanMain.setStatus("D");
        patientPlanMain.setActionStatus("D");
        patientPlanMain.setDeletedBy(responsiblePerson);
        patientPlanMain.setDeletedDate(l);
        patientPlanMain.getPatientPlans().forEach(plan -> {
            plan.setStatus("D");
            plan.setActionStatus("D");
            plan.setDeletedBy(responsiblePerson);
            plan.setDeletedDate(l);
        });
        patientPlanMainRepository.save(patientPlanMain);
    }
}
