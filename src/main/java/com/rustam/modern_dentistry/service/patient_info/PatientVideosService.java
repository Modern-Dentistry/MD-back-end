package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.repository.patient_info.PatientVideosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientVideosService {
    private final PatientVideosRepository patientVideosRepository;
}
