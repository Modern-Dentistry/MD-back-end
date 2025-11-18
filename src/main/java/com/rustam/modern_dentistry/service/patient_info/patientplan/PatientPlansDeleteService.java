package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlansRepository;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.util.PatientPlanUtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientPlansDeleteService {

    PatientPlansRepository patientPlansRepository;

    public void delete(UUID id) {
        PatientPlan patientPlan = patientPlansRepository.findByIdAndStatusInAndActionStatusIn(id, List.of("A", "C"), List.of("A", "C"))
                .orElseThrow(() -> new NotFoundException("No such patient plan found with id: " + id + " or it is deleted."));
        patientPlan.setStatus("A");
        patientPlan.setActionStatus("D");
        patientPlansRepository.save(patientPlan);
    }
}
