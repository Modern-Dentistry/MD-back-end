package com.rustam.modern_dentistry.util;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlanMainRepository;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientPlanMainUtilService {

    PatientPlanMainRepository patientPlanMainRepository;

    public PatientPlanMain findById(UUID id){
        return patientPlanMainRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such patient plan main found."));
    }
}
