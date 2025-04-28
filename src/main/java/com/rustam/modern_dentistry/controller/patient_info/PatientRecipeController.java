package com.rustam.modern_dentistry.controller.patient_info;

import com.rustam.modern_dentistry.dto.request.create.PatRecipeCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatRecipeUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatRecipeReadRes;
import com.rustam.modern_dentistry.service.patient_info.PatientRecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/patient-recipes")
@RequiredArgsConstructor
public class PatientRecipeController {
    private final PatientRecipeService patientRecipeService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody PatRecipeCreateReq req) {
        patientRecipeService.create(req);
        return ResponseEntity.status(CREATED).build();
    }

    @GetMapping("/read")
    public ResponseEntity<List<PatRecipeReadRes>> read(@RequestParam Long patientId) {
        return ResponseEntity.ok(patientRecipeService.read(patientId));
    }

    @GetMapping("/read/{patientId}")
    public ResponseEntity<List<PatRecipeReadRes>> readAllById(@PathVariable Long patientId) {
        return ResponseEntity.ok(patientRecipeService.readAllById(patientId));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody PatRecipeUpdateReq request) {
        patientRecipeService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientRecipeService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
