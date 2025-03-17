package com.rustam.modern_dentistry.controller;

import com.rustam.modern_dentistry.dto.request.create.InsuranceCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.UpdateICRequest;
import com.rustam.modern_dentistry.dto.response.read.InsuranceReadResponse;
import com.rustam.modern_dentistry.service.InsuranceCompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(path = "/api/v1/insurance-company")
@RequiredArgsConstructor
public class InsuranceCompanyController {
    private final InsuranceCompanyService insuranceCompanyService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody InsuranceCreateRequest request) {
        insuranceCompanyService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read")
    public ResponseEntity<List<InsuranceReadResponse>> read() {
        return ResponseEntity.ok(insuranceCompanyService.read());
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity<InsuranceReadResponse> read(@PathVariable Long id) {
        return ResponseEntity.ok(insuranceCompanyService.readById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateICRequest request) {
        insuranceCompanyService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/status/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id) {
        insuranceCompanyService.updateStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        insuranceCompanyService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
