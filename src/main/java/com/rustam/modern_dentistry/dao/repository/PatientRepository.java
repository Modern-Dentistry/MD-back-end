package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.users.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PatientRepository extends JpaRepository<Patient, Long> , JpaSpecificationExecutor<Patient> {
}
