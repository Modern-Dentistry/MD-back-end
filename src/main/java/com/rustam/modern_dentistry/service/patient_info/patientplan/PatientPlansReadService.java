package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlansRepository;
import com.rustam.modern_dentistry.dto.response.create.PatientPlansResponse;
import com.rustam.modern_dentistry.dto.response.read.CategoryOfOperationDto;
import com.rustam.modern_dentistry.dto.response.read.PatientPlanPartOfToothDetailDto;
import com.rustam.modern_dentistry.dto.response.read.PatientPlansDto;
import com.rustam.modern_dentistry.mapper.patient_info.patientplan.PatientPlansMapper;
import com.rustam.modern_dentistry.util.PatientPlanUtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientPlansReadService {

    PatientPlanUtilService patientPlanUtilService;
    PatientPlansRepository patientPlansRepository;

    @Transactional
    public List<PatientPlansResponse> read() {
        return patientPlansRepository.findAllByActionStatusInAndStatusIn(List.of("A","C"),List.of("A","C")).stream().map(patientPlanUtilService::mapper).toList();
    }

}
