package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.Reservation;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dao.repository.ReservationRepository;
import com.rustam.modern_dentistry.dto.request.create.ReservationCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.ReservationSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.ReservationUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.ReservationCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.ReservationReadResponse;
import com.rustam.modern_dentistry.dto.response.update.ReservationUpdateResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.ReservationMapper;
import com.rustam.modern_dentistry.util.ExcelUtil;
import com.rustam.modern_dentistry.util.UtilService;
import com.rustam.modern_dentistry.util.specification.ReservationSpec;
import com.rustam.modern_dentistry.util.specification.ReservationSpecification;
import com.rustam.modern_dentistry.util.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static com.rustam.modern_dentistry.dao.entity.enums.status.ReservationStatus.ACTIVE;
import static com.rustam.modern_dentistry.dao.entity.enums.status.ReservationStatus.PASSIVE;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final DoctorService doctorService;
    private final UtilService utilService;
    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationCreateResponse create(ReservationCreateRequest request) {
        var doctor = doctorService.findById(request.getDoctorId());
        var patient = utilService.findByPatientId(request.getPatientId());
        var queueReservation = reservationMapper.toEntity(request, doctor, patient);
        return reservationMapper.toCreateDto(reservationRepository.save(queueReservation));
    }

    public List<ReservationReadResponse> read() {
        var reservations = reservationRepository.findAll();
        return reservations.stream().map(reservationMapper::toReadDto).toList();
    }

    public ReservationReadResponse readById(Long id) {
        var reservation = getReservationById(id);
        return reservationMapper.toReadDto(reservation);
    }

    public ReservationUpdateResponse update(Long id, ReservationUpdateRequest request) {
        var reservation = getReservationById(id);
        var doctor = doctorService.findById(request.getDoctorId());
        var patient = utilService.findByPatientId(request.getPatientId());
        var updateReservation = reservationMapper.updateReservation(reservation, request, doctor, patient);
        return reservationMapper.toUpdateDto(reservationRepository.save(updateReservation));
    }

    public void updateStatus(Long id) {
        var reservation = getReservationById(id);
        var status = reservation.getStatus() == ACTIVE ? PASSIVE : ACTIVE;
        reservation.setStatus(status);
        reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        var reservation = getReservationById(id);
        reservationRepository.delete(reservation);
    }

    private Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də növbə tapımadı: " + id)
        );
    }
    @Transactional
    public List<ReservationReadResponse> search(ReservationSearchRequest request) {
        List<Reservation> reservations = reservationRepository.findAll(ReservationSpecification.filterBy(request));
        return reservations.stream().map(reservationMapper::toReadDto).toList();
    }

    public InputStreamResource exportReservationsToExcel() {
        List<Reservation> reservations = reservationRepository.findAll();
        var list = reservations.stream().map(reservationMapper::toReadDto).toList();
        try {
            ByteArrayInputStream excelFile = ExcelUtil.dataToExcel(list, ReservationReadResponse.class);
            return new InputStreamResource(excelFile);
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel file", e);
        }
    }
}
