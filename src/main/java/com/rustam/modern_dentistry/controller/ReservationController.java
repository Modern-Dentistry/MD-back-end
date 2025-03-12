package com.rustam.modern_dentistry.controller;

import com.rustam.modern_dentistry.dto.request.create.ReservationCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.PatientSearchRequest;
import com.rustam.modern_dentistry.dto.request.read.ReservationSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.ReservationUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.ReservationCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.PatientReadResponse;
import com.rustam.modern_dentistry.dto.response.read.ReservationReadResponse;
import com.rustam.modern_dentistry.dto.response.update.PatientUpdateResponse;
import com.rustam.modern_dentistry.dto.response.update.ReservationUpdateResponse;
import com.rustam.modern_dentistry.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<ReservationCreateResponse> create(@Valid @RequestBody ReservationCreateRequest request) {
        return ResponseEntity.status(CREATED).body(reservationService.create(request));
    }

    @GetMapping("/read")
    public ResponseEntity<List<ReservationReadResponse>> read() {
        return ResponseEntity.ok(reservationService.read());
    }

    @GetMapping("/read-by-id/{id}")
    public ResponseEntity<ReservationReadResponse> readById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.readById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReservationUpdateResponse> update(@PathVariable Long id,
                                                            @Valid @RequestBody ReservationUpdateRequest request) {
        return ResponseEntity.ok(reservationService.update(id, request));
    }

    @PatchMapping("/update/status/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id) {
        reservationService.updateStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @GetMapping( "/search")
    public ResponseEntity<List<ReservationReadResponse>> search(ReservationSearchRequest request){
        return ResponseEntity.ok(reservationService.search(request));
    }

//    @GetMapping(path = "/search")
//    public ResponseEntity<List<ReservationReadResponse>> search(@RequestBody ReservationSearchRequest request){
//        return ResponseEntity.ok(reservationService.search(request));
//    }

    @GetMapping("/export/excel")
    public ResponseEntity<InputStreamResource> exportToExcel(){
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reservations.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(reservationService.exportReservationsToExcel());
    }
}
