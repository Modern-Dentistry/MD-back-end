package com.rustam.modern_dentistry.controller;

import com.rustam.modern_dentistry.dto.request.create.NewAppointmentRequest;
import com.rustam.modern_dentistry.dto.response.create.NewAppointmentResponse;
import com.rustam.modern_dentistry.dto.response.read.GeneralCalendarResponse;
import com.rustam.modern_dentistry.dto.response.read.PatientReadResponse;
import com.rustam.modern_dentistry.dto.response.read.SelectingDoctorViewingPatientResponse;
import com.rustam.modern_dentistry.service.GeneralCalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/general-calendar")
@RequiredArgsConstructor
public class GeneralCalendarController {

    private final GeneralCalendarService generalCalendarService;

    @GetMapping(path = "/read-doctors")
    public ResponseEntity<List<GeneralCalendarResponse>> readDoctors() {
        return new ResponseEntity<>(generalCalendarService.readDoctors(), HttpStatus.OK);
    }

    @PostMapping(path = "/new-appointment")
    public ResponseEntity<NewAppointmentResponse> newAppointment(@Valid @RequestBody NewAppointmentRequest newAppointmentRequest){
        return new ResponseEntity<>(generalCalendarService.newAppointment(newAppointmentRequest),HttpStatus.OK);
    }

    @GetMapping(path = "/selecting-doctor-viewing-patient/{doctorId}")
    public ResponseEntity<List<SelectingDoctorViewingPatientResponse>> selectingDoctorViewingPatient(@PathVariable UUID doctorId){
        return new ResponseEntity<>(generalCalendarService.selectingDoctorViewingPatient(doctorId),HttpStatus.OK);
    }
}
