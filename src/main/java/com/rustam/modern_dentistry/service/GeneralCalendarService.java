package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.GeneralCalendar;
import com.rustam.modern_dentistry.dao.entity.enums.status.Room;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dao.repository.GeneralCalendarRepository;
import com.rustam.modern_dentistry.dto.request.create.NewAppointmentRequest;
import com.rustam.modern_dentistry.dto.request.update.UpdateAppointmentRequest;
import com.rustam.modern_dentistry.dto.response.create.NewAppointmentResponse;
import com.rustam.modern_dentistry.dto.response.read.GeneralCalendarResponse;
import com.rustam.modern_dentistry.dto.response.read.SelectingDoctorViewingPatientResponse;
import com.rustam.modern_dentistry.dto.response.read.SelectingPatientToReadResponse;
import com.rustam.modern_dentistry.exception.custom.DoctorIsPatientsWereNotFound;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NoSuchPatientWasFound;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
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
        List<SelectingDoctorViewingPatientResponse> allByDoctorId = generalCalendarRepository.findAllByDoctorId(doctorId);
        if (allByDoctorId.isEmpty()){
            throw new DoctorIsPatientsWereNotFound("pasientler tapilmadi");
        }
        return allByDoctorId;
    }

    public SelectingPatientToReadResponse selectingPatientToRead(Long patientId) {
        return generalCalendarRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NoSuchPatientWasFound("bele bir pasient tapilmadi"));
    }

    public List<SelectingDoctorViewingPatientResponse> selectingRoomViewingPatient(Room room) {
        List<SelectingDoctorViewingPatientResponse> allRoom = generalCalendarRepository.findAllRoom(room);
        if (allRoom.isEmpty()){
            throw new DoctorIsPatientsWereNotFound("pasientler tapilmadi");
        }
        return allRoom;
    }


    public String delete(Long id) {
        GeneralCalendar generalCalendar = findById(id);
        String name = generalCalendar.getPatient().getName();
        generalCalendarRepository.delete(generalCalendar);
        return "Bu :"+ name +"pasient silindi";
    }

    @Transactional
    public NewAppointmentResponse update(UpdateAppointmentRequest updateAppointmentRequest) {
        GeneralCalendar generalCalendar = findById(updateAppointmentRequest.getId());
        if (updateAppointmentRequest.getDoctorId() != null) {
            Doctor doctor = doctorService.findById(updateAppointmentRequest.getDoctorId());
            generalCalendar.setDoctor(doctor);
        }
        if (updateAppointmentRequest.getPatientId() != null) {
            Patient patient = utilService.findByPatientId(updateAppointmentRequest.getPatientId());
            generalCalendar.setPatient(patient);
        }
        if (updateAppointmentRequest.getRoom() != null) {
            generalCalendar.setRoom(updateAppointmentRequest.getRoom());
        }
        if (updateAppointmentRequest.getAppointment() != null) {
            generalCalendar.setAppointment(updateAppointmentRequest.getAppointment());
        }
        if (updateAppointmentRequest.getDate() != null) {
            generalCalendar.setDate(updateAppointmentRequest.getDate());
        }
        if (updateAppointmentRequest.getTime() != null) {
            generalCalendar.setTime(updateAppointmentRequest.getTime());
        }
        if (updateAppointmentRequest.getPeriod() != null) {
            generalCalendar.setPeriod(updateAppointmentRequest.getPeriod());
        }
        generalCalendarRepository.save(generalCalendar);

        return NewAppointmentResponse.builder()
                .doctorName(generalCalendar.getDoctor().getName())
                .patientName(generalCalendar.getPatient().getName())
                .room(generalCalendar.getRoom())
                .appointment(generalCalendar.getAppointment())
                .date(generalCalendar.getDate())
                .time(generalCalendar.getTime())
                .period(generalCalendar.getPeriod())
                .build();
    }

    public GeneralCalendar findById(Long id) {
        return generalCalendarRepository.findById(id)
                .orElseThrow(() -> new NoSuchPatientWasFound("bele bir pasient tapilmadi"));
    }

    public SelectingPatientToReadResponse findByPatientId(Long patientId) {
        return generalCalendarRepository.findByPatientId(patientId)
                .orElseThrow(() -> new NoSuchPatientWasFound("bele bir patient tapilmadi"));
    }
}
