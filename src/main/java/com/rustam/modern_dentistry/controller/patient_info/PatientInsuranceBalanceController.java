package com.rustam.modern_dentistry.controller.patient_info;

import com.rustam.modern_dentistry.dto.request.create.PatInsuranceBalanceCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatInsuranceBalanceUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatInsuranceBalanceReadResponse;
import com.rustam.modern_dentistry.service.patient_info.PatientInsuranceBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/patient-insurance-balance")
@RequiredArgsConstructor
public class PatientInsuranceBalanceController {
    private final PatientInsuranceBalanceService patientInsuranceBalanceService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody PatInsuranceBalanceCreateReq request) {
        patientInsuranceBalanceService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read")
    public ResponseEntity<List<PatInsuranceBalanceReadResponse>> read(@RequestParam Long patientInsuranceId) {
        return ResponseEntity.ok(patientInsuranceBalanceService.read(patientInsuranceId));
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity<PatInsuranceBalanceReadResponse> readById(@PathVariable Long id) {
        return ResponseEntity.ok(patientInsuranceBalanceService.readById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody PatInsuranceBalanceUpdateReq request) {
        patientInsuranceBalanceService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update-status/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id) {
        patientInsuranceBalanceService.updateStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientInsuranceBalanceService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
