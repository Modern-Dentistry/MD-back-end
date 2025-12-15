package com.rustam.modern_dentistry.controller.patient_info.patientplan;

import com.rustam.modern_dentistry.dto.request.create.PatientPlansMainCreateRequest;
import com.rustam.modern_dentistry.dto.request.save.PatientPlansSaveRequest;
import com.rustam.modern_dentistry.dto.request.update.PatientPlansMainUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.PatientPlansMainResponse;
import com.rustam.modern_dentistry.dto.response.read.ReadPatientsInsuranceResponse;
import com.rustam.modern_dentistry.service.patient_info.patientplan.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patient-plans-main")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PatientPlansMainController {

    PatientPlansCreateMainService patientPlansCreateMainService;
    PatientPlansReadMainService patientPlansReadMainService;
    PatientPlansUpdateMainService patientPlansUpdateMainService;
    PatientPlansDeleteMainService patientPlansDeleteMainService;
    PatientPlansSaveMainService patientPlansSaveMainService;
    PatientPlansReadByPatientMainService patientPlansReadByPatientMainService;
    PatientPlansReadPatientsInsuranceMainService patientPlansReadPatientsInsuranceMainService;

    @PostMapping(path = "/create")
    public ResponseEntity<PatientPlansMainResponse> createMain(@RequestBody PatientPlansMainCreateRequest req) {
        return new ResponseEntity<>(patientPlansCreateMainService.create(req), HttpStatus.CREATED);
    }

    @GetMapping(path = "/read")
    public ResponseEntity<List<PatientPlansMainResponse>> readMain() {
        return new ResponseEntity<>(patientPlansReadMainService.read(), HttpStatus.OK);
    }

    @GetMapping(path = "/read/{patientId}")
    public ResponseEntity<List<PatientPlansMainResponse>> readMainByPatientId(@PathVariable Long patientId) {
        return new ResponseEntity<>(patientPlansReadByPatientMainService.readByPatientId(patientId),HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<PatientPlansMainResponse> updateMain(@RequestBody PatientPlansMainUpdateRequest patientPlansMainUpdateRequest) {
        return new ResponseEntity<>(patientPlansUpdateMainService.update(patientPlansMainUpdateRequest), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteMain(@PathVariable UUID id) {
        patientPlansDeleteMainService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/save/{id}")
    public ResponseEntity<Void> save(@PathVariable UUID id) {
        patientPlansSaveMainService.save(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/read-patients-insurance/{patientInsuranceId}")
    public ResponseEntity<List<String>> readPatientsInsurance(@PathVariable Long patientInsuranceId){
        return new ResponseEntity<>(patientPlansReadPatientsInsuranceMainService.readPatientsInsurance(patientInsuranceId),HttpStatus.OK);
    }

}