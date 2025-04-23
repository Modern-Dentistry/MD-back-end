package com.rustam.modern_dentistry.controller.patient_info;

import com.rustam.modern_dentistry.service.patient_info.PatientXrayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/patient-xray")
@RequiredArgsConstructor
public class PatientXrayController {
    private final PatientXrayService patientXrayService;
}
