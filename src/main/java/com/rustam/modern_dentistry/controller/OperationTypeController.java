package com.rustam.modern_dentistry.controller;

import com.rustam.modern_dentistry.dto.request.create.OpTypeCreateRequest;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.read.OperationTypeSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.OpTypeUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.InsDeducReadResponse;
import com.rustam.modern_dentistry.dto.response.read.OperationTypeReadResponse;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.service.OperationTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/operation-types")
@RequiredArgsConstructor
public class OperationTypeController {
    private final OperationTypeService operationTypeService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody OpTypeCreateRequest request) {
        operationTypeService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read")
    public ResponseEntity<PageResponse<OperationTypeReadResponse>> read(PageCriteria pageCriteria) {
        return ResponseEntity.ok(operationTypeService.read(pageCriteria));
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity<OperationTypeReadResponse> readById(@PathVariable Long id) {
        return ResponseEntity.ok(operationTypeService.readById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody OpTypeUpdateRequest request) {
        operationTypeService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/status/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id) {
        operationTypeService.updateStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        operationTypeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<OperationTypeReadResponse>> search(OperationTypeSearchRequest request, PageCriteria criteria) {
        return ResponseEntity.ok(operationTypeService.search(request, criteria));
    }

    @GetMapping("/insurance-deductible/{id}")
    public ResponseEntity<List<InsDeducReadResponse>> getInsuranceDeductibles(@PathVariable  Long id) {
        return ResponseEntity.ok(operationTypeService.getInsuranceDeductibles(id));
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=operation_type.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(operationTypeService.exportReservationsToExcel());
    }
}
