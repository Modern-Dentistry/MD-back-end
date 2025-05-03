package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.PatientBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientBlacklistRepository extends JpaRepository<PatientBlacklist, Long> {
}
