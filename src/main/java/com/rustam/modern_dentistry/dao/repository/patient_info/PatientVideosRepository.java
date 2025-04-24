package com.rustam.modern_dentistry.dao.repository.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientVideos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientVideosRepository extends JpaRepository<PatientVideos, Long> {
    List<PatientVideos> findAllByPatient_Id(Long patientId);
}
