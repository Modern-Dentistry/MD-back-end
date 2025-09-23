package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, JpaSpecificationExecutor<Reservation> {

    @EntityGraph(attributePaths = {"baseUser", "patient"})
    List<Reservation> findAll();

    @EntityGraph(attributePaths = {"baseUser", "patient"})
    Page<Reservation> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"baseUser", "patient"})
    Optional<Reservation> findById(Long id);
}
