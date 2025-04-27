package com.rustam.modern_dentistry.controller.patient_info;

import com.rustam.modern_dentistry.service.patient_info.PatientRecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patient-recipes")
@RequiredArgsConstructor
public class PatientRecipeController {
    private final PatientRecipeService patientRecipeService;
}
