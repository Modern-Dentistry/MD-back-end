package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.repository.patient_info.PatientXrayRepository;
import com.rustam.modern_dentistry.mapper.patient_info.PatientXrayMapper;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientXrayService {
    private final UtilService utilService;
    private final PatientXrayMapper patientXrayMapper;
    private final PatientXrayRepository patientXrayRepository;
}
