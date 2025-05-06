package com.rustam.modern_dentistry.controller;

import com.rustam.modern_dentistry.dto.request.create.TechnicianCreateRequest;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.read.ReservationSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.TechnicianUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.dto.response.read.ReservationReadResponse;
import com.rustam.modern_dentistry.dto.response.read.TechnicianReadResponse;
import com.rustam.modern_dentistry.service.TechnicianService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(path = "/api/v1/technician")
@RequiredArgsConstructor
public class TechnicianController {
    private final TechnicianService technicianService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody TechnicianCreateRequest request) {
        technicianService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read")
    public ResponseEntity<PageResponse<TechnicianReadResponse>> read(PageCriteria pageCriteria) {
        return ResponseEntity.ok(technicianService.read(pageCriteria));
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity<TechnicianReadResponse> readById(@PathVariable Long id) {
        return ResponseEntity.ok(technicianService.readById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody TechnicianUpdateRequest request) {
        technicianService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update/status/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id) {
        technicianService.updateStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        technicianService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<ReservationReadResponse>> search(ReservationSearchRequest request, PageCriteria pageCriteria) {
        return ResponseEntity.ok(technicianService.search(request, pageCriteria));
    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel() {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reservations.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(technicianService.exportReservationsToExcel());
    }
}
