package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlanMainRepository;
import com.rustam.modern_dentistry.dto.response.read.PatientPlansMainResponse;
import com.rustam.modern_dentistry.mapper.patient_info.patientplan.PatientPlansMainMapper;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientPlansReadMainService {

    PatientPlanMainRepository patientPlanMainRepository;
    UtilService utilService;
    PatientPlansMainMapper patientPlansMainMapper;

    @Transactional
    public List<PatientPlansMainResponse> read() {
        return patientPlansMainMapper.toDtos(
                patientPlanMainRepository.findAllByStatusAndActionStatus("A","A")
        );
    }
}
