package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientExaminations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatientExaminationsRepository extends JpaRepository<PatientExaminations, Long> {
    @Query("SELECT CASE WHEN COUNT(pe) > 0 THEN true ELSE false END FROM PatientExaminations pe " +
            "WHERE pe.patient.id = :patientId AND pe.toothNumber IN :toothNumber")
    boolean existsPatientExaminationsByPatientAndToothNumber(Long patientId, List<Long> toothNumber);
}
