package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.GeneralCalendar;
import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.entity.enums.status.Room;
import com.rustam.modern_dentistry.dao.entity.settings.AppointmentType;
import com.rustam.modern_dentistry.dao.entity.settings.Cabinet;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dao.repository.GeneralCalendarRepository;
import com.rustam.modern_dentistry.dto.request.create.AppointmentTypeRequestId;
import com.rustam.modern_dentistry.dto.request.create.NewAppointmentRequest;
import com.rustam.modern_dentistry.dto.request.update.UpdateAppointmentRequest;
import com.rustam.modern_dentistry.dto.response.create.NewAppointmentResponse;
import com.rustam.modern_dentistry.dto.response.read.*;
import com.rustam.modern_dentistry.exception.custom.DoctorIsPatientsWereNotFound;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NoSuchPatientWasFound;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.GeneralCalendarMapper;
import com.rustam.modern_dentistry.service.settings.AppointmentTypeService;
import com.rustam.modern_dentistry.service.settings.CabinetService;
import com.rustam.modern_dentistry.util.UtilService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeneralCalendarService {

    GeneralCalendarRepository generalCalendarRepository;
    DoctorService doctorService;
    UtilService utilService;
    AppointmentTypeService appointmentTypeService;
    CabinetService cabinetService;
    PatientService patientService;
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

    @Transactional
    public NewAppointmentResponse newAppointment(NewAppointmentRequest newAppointmentRequest) {
        Patient patient = utilService.findByPatientId(newAppointmentRequest.getPatientId());
        boolean existsByPatientId = generalCalendarRepository.existsActivePatientById(patient.getId());
        Cabinet cabinet = cabinetService.findByCabinetName(newAppointmentRequest.getCabinetName());
        if (!existsByPatientId) {
            throw new ExistsException("Bu patient artiq randevu goturub");
        }

        Set<AppointmentType> appointmentTypes = newAppointmentRequest.getAppointmentTypeRequestIds().stream()
                .map(AppointmentTypeRequestId::getId)
                .map(appointmentTypeService::findById)
                .collect(Collectors.toSet());

        GeneralCalendar generalCalendar = GeneralCalendar.builder()
                .doctor(patient.getDoctor())
                .patient(patient)
                .appointment(newAppointmentRequest.getAppointment())
                .cabinet(cabinet)
                .date(newAppointmentRequest.getDate())
                .time(newAppointmentRequest.getTime())
                .appointmentTypes(appointmentTypes)
                .period(newAppointmentRequest.getPeriod())
                .build();
        generalCalendarRepository.save(generalCalendar);

        List<AppointmentTypeResponse> appointmentTypeResponses = appointmentTypes.stream()
                .map(type -> AppointmentTypeResponse.builder()
                        .id(type.getId())
                        .appointmentTypeName(type.getAppointmentTypeName())
                        .time(type.getTime())
                        .status(type.getStatus())
                        .build())
                .toList();

        return new NewAppointmentResponse(
                patient.getDoctor().getName(),
                generalCalendar.getCabinet().getCabinetName(),
                patient.getName(),
                generalCalendar.getAppointment(),
                generalCalendar.getDate(),
                appointmentTypeResponses,
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

    public List<SelectingDoctorViewingPatientResponse> selectingRoomViewingPatient(String cabinetName) {
        List<SelectingDoctorViewingPatientResponse> allRoom = generalCalendarRepository.findAllCabinetName(cabinetName);
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
            generalCalendar.setDoctor(doctorService.findById(updateAppointmentRequest.getDoctorId()));
        }
        if (updateAppointmentRequest.getPatientId() != null) {
            generalCalendar.setPatient(utilService.findByPatientId(updateAppointmentRequest.getPatientId()));
        }
        if (updateAppointmentRequest.getCabinetName() != null) {
            generalCalendar.setCabinet(cabinetService.findByCabinetName(updateAppointmentRequest.getCabinetName()));
        }
        utilService.updateFieldIfPresent(updateAppointmentRequest.getAppointment(), generalCalendar::setAppointment);
        utilService.updateFieldIfPresent(updateAppointmentRequest.getDate(), generalCalendar::setDate);
        utilService.updateFieldIfPresent(updateAppointmentRequest.getTime(), generalCalendar::setTime);
        utilService.updateFieldIfPresent(updateAppointmentRequest.getPeriod(), generalCalendar::setPeriod);
        generalCalendarRepository.save(generalCalendar);

        return NewAppointmentResponse.builder()
                .doctorName(generalCalendar.getDoctor().getName())
                .patientName(generalCalendar.getPatient().getName())
                .cabinetName(generalCalendar.getCabinet().getCabinetName())
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

    public List<ReadRooms> read() {
        List<Cabinet> cabinets = cabinetService.findAllByCabinetName();
        return generalCalendarMapper.toCabinetDto(cabinets.stream()
            .filter(cabinet -> cabinet.getCabinetName() != null)
            .collect(Collectors.toList()));
}
}