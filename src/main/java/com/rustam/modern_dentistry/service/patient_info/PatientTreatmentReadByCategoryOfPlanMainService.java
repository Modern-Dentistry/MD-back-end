package com.rustam.modern_dentistry.service.patient_info;


import com.rustam.modern_dentistry.dto.response.read.CategoryOfOperationDto;
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
public class PatientTreatmentReadByCategoryOfPlanMainService {

    PatientPlanUtilService patientPlanUtilService;

    public List<CategoryOfOperationDto> read(UUID id) {
        return patientPlanUtilService.readByCategorysOperationForTreatment(id);
    }
}
