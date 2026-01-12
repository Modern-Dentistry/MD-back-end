package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dto.request.create.PatientTreatmentRequest;
import com.rustam.modern_dentistry.dto.response.read.ReadByPatientPlanMainIdOfTreatment;
import com.rustam.modern_dentistry.util.PatientPlanUtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientTreatmentReadByPlanMainService {

    PatientPlanUtilService patientPlanUtilService;

    @Transactional
    public ReadByPatientPlanMainIdOfTreatment read(UUID planMainId) {
        return patientPlanUtilService.read(planMainId);
    }
}
