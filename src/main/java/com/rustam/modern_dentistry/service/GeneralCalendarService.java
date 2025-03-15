package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.GeneralCalendar;
import com.rustam.modern_dentistry.dao.entity.enums.status.Room;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dao.repository.GeneralCalendarRepository;
import com.rustam.modern_dentistry.dto.request.create.NewAppointmentRequest;
import com.rustam.modern_dentistry.dto.response.create.NewAppointmentResponse;
import com.rustam.modern_dentistry.dto.response.read.GeneralCalendarResponse;
import com.rustam.modern_dentistry.dto.response.read.SelectingDoctorViewingPatientResponse;
import com.rustam.modern_dentistry.dto.response.read.SelectingPatientToReadResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.util.UtilService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeneralCalendarService {

    GeneralCalendarRepository generalCalendarRepository;
    DoctorService doctorService;
    UtilService utilService;

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

    @Transactional
    public NewAppointmentResponse newAppointment(NewAppointmentRequest newAppointmentRequest) {
        Doctor doctor = doctorService.findById(newAppointmentRequest.getDoctorId());
        Patient patient = utilService.findByPatientId(newAppointmentRequest.getPatientId());
        boolean existsByPatientId = generalCalendarRepository.existsActivePatientById(patient.getId());
        if (!existsByPatientId) {
            throw new ExistsException("Bu patient artiq randevu goturub");
        }
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
        return new NewAppointmentResponse(
                doctor.getName(),
                generalCalendar.getRoom(),
                patient.getName(),
                generalCalendar.getAppointment(),
                generalCalendar.getDate(),
                generalCalendar.getTime(),
                generalCalendar.getPeriod()
        );
    }

    public List<SelectingDoctorViewingPatientResponse> selectingDoctorViewingPatient(UUID doctorId) {
        return generalCalendarRepository.findAllByDoctorId(doctorId);
    }

    public SelectingPatientToReadResponse selectingPatientToRead(Long patientId) {
        return generalCalendarRepository.findByPatientId(patientId);
    }

    public List<SelectingDoctorViewingPatientResponse> selectingRoomViewingPatient(Room room) {
        return generalCalendarRepository.findAllRoom(room);
    }
}
