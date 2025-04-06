package com.rustam.modern_dentistry.dao.repository.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientInsuranceBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PatientInsuranceBalanceRepository extends JpaRepository<PatientInsuranceBalance, Long> {

    boolean existsByDate(LocalDate date);

    List<PatientInsuranceBalance> findAllByPatientInsurance_Id(Long patientInsuranceId);

}
