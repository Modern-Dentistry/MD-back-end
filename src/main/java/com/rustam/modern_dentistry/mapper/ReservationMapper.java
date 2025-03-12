package com.rustam.modern_dentistry.mapper;

import com.rustam.modern_dentistry.dao.entity.Reservation;
import com.rustam.modern_dentistry.dao.entity.enums.status.ReservationStatus;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dto.request.create.ReservationCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.ReservationUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.ReservationCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.ReservationReadResponse;
import com.rustam.modern_dentistry.dto.response.update.ReservationUpdateResponse;
import org.springframework.stereotype.Component;

import static com.rustam.modern_dentistry.dao.entity.enums.status.ReservationStatus.ACTIVE;

@Component
public class ReservationMapper {

    public Reservation toEntity(ReservationCreateRequest request, Doctor doctor, Patient patient) {

        return Reservation.builder()
                .status(ACTIVE)
                .doctor(doctor)
                .patient(patient)
                .weekDays(request.getWeekDays())
                .endTime(request.getEndTime())
                .endDate(request.getEndDate())
                .startDate(request.getStartDate())
                .startTime(request.getStartTime())
                .build();
    }

    public ReservationCreateResponse toCreateDto(Reservation reservation) {

        return ReservationCreateResponse.builder()
                .id(reservation.getId())
                .endDate(reservation.getEndDate())
                .startDate(reservation.getStartDate())
                .endTime(reservation.getEndTime())
                .startTime(reservation.getStartTime())
                .weekDays(reservation.getWeekDays())
                .patientId(reservation.getPatient().getId())
                .doctorId(reservation.getDoctor().getId())
                .status(reservation.getStatus())
                .build();
    }

    public ReservationReadResponse toReadDto(Reservation reservation) {

        return ReservationReadResponse.builder()
                .id(reservation.getId())
                .mobilePhone(reservation.getPatient().getPhone())
                .endDate(reservation.getEndDate())
                .startDate(reservation.getStartDate())
                .endTime(reservation.getEndTime())
                .startTime(reservation.getStartTime())
                .patientName(reservation.getPatient().getName())
                .patientSurname(reservation.getPatient().getSurname())
                .doctorName(reservation.getDoctor().getName())
                .doctorSurname(reservation.getDoctor().getSurname())
                .status(reservation.getStatus())
                .build();
    }

    public ReservationUpdateResponse toUpdateDto(Reservation reservation) {

        return ReservationUpdateResponse.builder()
                .endDate(reservation.getEndDate())
                .startDate(reservation.getStartDate())
                .endTime(reservation.getEndTime())
                .startTime(reservation.getStartTime())
                .patientId(reservation.getPatient().getId())
                .doctorId(reservation.getDoctor().getId())
                .build();
    }

    public Reservation updateReservation(Reservation reservation, ReservationUpdateRequest request, Doctor doctor, Patient patient) {

        if (request == null) return reservation;

        return new Reservation(
                null,
                request.getStartDate() == null ? reservation.getStartDate() : request.getStartDate(),
                request.getEndDate() == null ? reservation.getEndDate() : request.getEndDate(),
                request.getStartTime() == null ? reservation.getStartTime() : request.getStartTime(),
                request.getEndTime() == null ? reservation.getEndTime() : request.getEndTime(),
                request.getWeekDays() == null ? reservation.getWeekDays() : request.getWeekDays(),
                request.getDoctorId() == null ? reservation.getDoctor() : doctor,
                request.getPatientId() == null ? reservation.getPatient() : patient,
                reservation.getStatus()
        );
    }

}
