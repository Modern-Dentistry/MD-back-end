package com.rustam.modern_dentistry.controller.patient_info;


import com.rustam.modern_dentistry.dto.request.create.PatientTreatmentRequest;
import com.rustam.modern_dentistry.dto.request.save.PatientTreatmentSaveRequest;
import com.rustam.modern_dentistry.dto.response.read.ReadByPatientPlanMainIdOfTreatment;
import com.rustam.modern_dentistry.service.patient_info.PatientTreatmentCreateService;
import com.rustam.modern_dentistry.service.patient_info.PatientTreatmentReadByPlanMainService;
import com.rustam.modern_dentistry.service.patient_info.PatientTreatmentSaveService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patient-treatment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientTreatmentController {

    PatientTreatmentCreateService service;
    PatientTreatmentReadByPlanMainService patientTreatmentReadByPlanMainService;
    PatientTreatmentSaveService patientTreatmentSaveService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody PatientTreatmentRequest req){
        service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(path = "/read-by-plan-main-id-of-treatment/{planMainId}")
    public ResponseEntity<ReadByPatientPlanMainIdOfTreatment> read(@PathVariable UUID planMainId){
        return new ResponseEntity<>(patientTreatmentReadByPlanMainService.read(planMainId),HttpStatus.OK);
    }

    @PostMapping(path = "/save")
    public ResponseEntity<Void> save(@RequestBody PatientTreatmentSaveRequest patientTreatmentSaveRequest){
        patientTreatmentSaveService.save(patientTreatmentSaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
