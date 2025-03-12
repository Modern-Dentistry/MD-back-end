package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.GeneralCalendar;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dao.repository.GeneralCalendarRepository;
import com.rustam.modern_dentistry.dto.request.create.NewAppointmentRequest;
import com.rustam.modern_dentistry.dto.response.create.NewAppointmentResponse;
import com.rustam.modern_dentistry.dto.response.read.GeneralCalendarResponse;
import com.rustam.modern_dentistry.dto.response.read.PatientReadResponse;
import com.rustam.modern_dentistry.mapper.GeneralCalendarMapper;
import com.rustam.modern_dentistry.util.UtilService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class GeneralCalendarService {

    GeneralCalendarRepository generalCalendarRepository;
    DoctorService doctorService;
    UtilService utilService;
    GeneralCalendarMapper generalCalendarMapper;
    
    public List<GeneralCalendarResponse> readDoctors() {
        List<Doctor> doctors = doctorService.readAll();
        return doctors.stream()
                .map(doctor -> new GeneralCalendarResponse(
                        doctor.getId(),
                        doctor.getName(),
                        doctor.getSurname(),
                        doctor.getUsername()
                ))
                .toList();
    }

    public NewAppointmentResponse newAppointment(NewAppointmentRequest newAppointmentRequest) {
        Doctor doctor = doctorService.findById(newAppointmentRequest.getDoctorId());
        Patient patient = utilService.findByPatientId(newAppointmentRequest.getPatientId());
        GeneralCalendar generalCalendar = GeneralCalendar.builder()
                .doctor(doctor)
                .patient(patient)
                .appointment(newAppointmentRequest.getAppointment())
                .room(newAppointmentRequest.getRoom())
                .date(newAppointmentRequest.getDate())
                .time(newAppointmentRequest.getTime())
                .period(newAppointmentRequest.getPeriod())
                .build();
        generalCalendarRepository.save(generalCalendar);
        return generalCalendarMapper.toDto(generalCalendar);
    }

    public List<PatientReadResponse> selectingDoctorViewingPatient(UUID doctorId) {
        Doctor doctor = doctorService.findById(doctorId);
        return doctor.getPatients().stream()
                .map(this::convertToDto)
                .toList();
    }

    private PatientReadResponse convertToDto(Patient patient) {
        return PatientReadResponse.builder()
                .id(patient.getId())
                .name(patient.getName())
                .surname(patient.getSurname())
                .email(patient.getEmail())
                .patronymic(patient.getPatronymic())
                .phone(patient.getPhone())
                .finCode(patient.getFinCode())
                .genderStatus(patient.getGenderStatus())
                .dateOfBirth(patient.getDateOfBirth())
                .specializationStatus(patient.getSpecializationStatus())
                .priceCategoryStatus(patient.getPriceCategoryStatus())
                .workAddress(patient.getWorkAddress())
                .homePhone(patient.getHomePhone())
                .homeAddress(patient.getHomeAddress())
                .workPhone(patient.getWorkPhone())
                .workAddress(patient.getWorkAddress())
                .build();

    }
}
