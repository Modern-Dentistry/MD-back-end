package com.rustam.modern_dentistry.controller.patient_info.patientplan;

import com.rustam.modern_dentistry.dto.request.create.PatientPlansCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.PatientPlansReadCategoryAndOperationsRequest;
import com.rustam.modern_dentistry.dto.request.update.PatientPlanUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.PatientPlansResponse;
import com.rustam.modern_dentistry.dto.response.read.CategoryOfOperationDto;
import com.rustam.modern_dentistry.service.patient_info.PatientPlansReadCategoryAndOperationsService;
import com.rustam.modern_dentistry.service.patient_info.patientplan.PatientPlansCreateService;
import com.rustam.modern_dentistry.service.patient_info.patientplan.PatientPlansDeleteService;
import com.rustam.modern_dentistry.service.patient_info.patientplan.PatientPlansReadService;
import com.rustam.modern_dentistry.service.patient_info.patientplan.PatientPlansUpdateService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patient-plans")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientPlansController {

    PatientPlansReadCategoryAndOperationsService patientPlansReadCategoryAndOperationsService;
    PatientPlansCreateService patientPlansCreateService;
    PatientPlansReadService patientPlansReadService;
    PatientPlansUpdateService patientPlansUpdateService;
    PatientPlansDeleteService patientPlansDeleteService;

    @PostMapping(path = "/create")
    public ResponseEntity<PatientPlansResponse> create(@RequestBody PatientPlansCreateRequest req){
        return new ResponseEntity<>(patientPlansCreateService.create(req),HttpStatus.CREATED);
    }

    @PostMapping(path = "/read-category-and-operations")
    public ResponseEntity<List<CategoryOfOperationDto>> readCategoryAndOperations(@RequestBody PatientPlansReadCategoryAndOperationsRequest patientPlansReadCategoryAndOperationsRequest){
        return new ResponseEntity<>(patientPlansReadCategoryAndOperationsService.readCategoryAndOperations(patientPlansReadCategoryAndOperationsRequest), HttpStatus.OK);
    }

    @GetMapping(path = "/read")
    public ResponseEntity<List<PatientPlansResponse>> read(){
        return new ResponseEntity<>(patientPlansReadService.read(),HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<PatientPlansResponse> update(@RequestBody PatientPlanUpdateRequest req){
        return new ResponseEntity<>(patientPlansUpdateService.update(req),HttpStatus.OK);
    }

    @PostMapping(path = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        patientPlansDeleteService.delete(id);
        return ResponseEntity.ok().build();
    }
}
