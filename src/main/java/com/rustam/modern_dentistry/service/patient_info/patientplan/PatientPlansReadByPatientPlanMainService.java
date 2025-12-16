package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlansRepository;
import com.rustam.modern_dentistry.dto.response.create.PatientPlansResponse;
import com.rustam.modern_dentistry.dto.response.read.PatientPlansDto;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.patient_info.patientplan.PatientPlansMainMapper;
import com.rustam.modern_dentistry.util.PatientPlanUtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientPlansReadByPatientPlanMainService {

    PatientPlansRepository patientPlansRepository;
    PatientPlanUtilService patientPlanUtilService;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<PatientPlansResponse> readByPatientPlanMainId(UUID id) {
        return patientPlansRepository
                .findAllByPatientPlanMainIdAndStatusInAndActionStatusIn(id,List.of("A","C"),List.of("A","C"))
                .stream().map(patientPlanUtilService::mapper).toList();
    }
}
