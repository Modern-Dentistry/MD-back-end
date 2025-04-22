package com.rustam.modern_dentistry.dao.repository.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientPhotos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientPhotosRepository extends JpaRepository<PatientPhotos, Long> {
}
