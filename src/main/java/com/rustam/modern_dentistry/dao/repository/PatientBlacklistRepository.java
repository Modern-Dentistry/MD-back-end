package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.PatientBlacklist;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientBlacklistRepository extends JpaRepository<PatientBlacklist, Long> {

    boolean existsByPatientId(@NotNull Long patientId);

    @EntityGraph(attributePaths = {"patient", "blacklistResult"})
    @NotNull
    List<PatientBlacklist> findAll();

    @EntityGraph(attributePaths = {"patient", "blacklistResult"})
    @NotNull
    Page<PatientBlacklist> findAll(@NotNull Pageable pageable);
}
