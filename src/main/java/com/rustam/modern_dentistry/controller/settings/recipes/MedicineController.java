package com.rustam.modern_dentistry.controller.settings.recipes;

import com.rustam.modern_dentistry.dto.request.create.MedicineCreateRequest;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.update.MedicineUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.MedicineReadResponse;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.service.settings.recipes.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(path = "/api/v1/medicine")
@RequiredArgsConstructor
public class MedicineController {
    private final MedicineService medicineService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody MedicineCreateRequest request) {
        medicineService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read")
    public ResponseEntity<PageResponse<MedicineReadResponse>> read(
            PageCriteria pageCriteria,
            @RequestParam Long recipeId) {
        return ResponseEntity.ok(medicineService.read(recipeId, pageCriteria));
    }

    @GetMapping("/read-by-id")
    public ResponseEntity<MedicineReadResponse> readById(@RequestParam Long id) {
        return ResponseEntity.ok(medicineService.readById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody MedicineUpdateRequest request) {
        medicineService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update-status/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id) {
        medicineService.updateStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicineService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
