package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

    @EntityGraph(attributePaths = {"doctor", "patient"})
    List<Reservation> findAll();
}
