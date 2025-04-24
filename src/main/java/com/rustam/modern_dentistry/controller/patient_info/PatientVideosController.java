package com.rustam.modern_dentistry.controller.patient_info;

import com.rustam.modern_dentistry.service.patient_info.PatientVideosService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/patient-videos")
@RequiredArgsConstructor
public class PatientVideosController {
    private final PatientVideosService patientVideosService;
}
