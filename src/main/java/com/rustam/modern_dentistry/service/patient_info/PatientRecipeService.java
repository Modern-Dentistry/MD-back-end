package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.repository.PatientRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientRecipeService {
    private final PatientRecipeRepository patientRecipeRepository;
}
