package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.patienttreatment.PatientTreatment;
import com.rustam.modern_dentistry.dao.repository.patient_info.PatientTreatmentRepository;
import com.rustam.modern_dentistry.dto.request.save.PatientTreatmentSaveRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientTreatmentSaveService {

    PatientTreatmentRepository patientTreatmentRepository;

    @Transactional
    public void save(PatientTreatmentSaveRequest req) {
        List<PatientTreatment> patientTreatments = patientTreatmentRepository.findByIdInAndIsCheckedAndStatusInAndActionStatusIn(req.getCheckedPlanIds(), true, List.of("A", "C"), List.of("A", "C"));
        patientTreatments.forEach(patientTreatment ->{
            patientTreatment.setStatus("A");
            patientTreatment.setActionStatus("A");
                }
        );
        patientTreatmentRepository.saveAll(patientTreatments);
    }
}
