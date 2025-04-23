package com.rustam.modern_dentistry.dao.repository.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientXray;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientXrayRepository extends JpaRepository<PatientXray, Long> {
}
