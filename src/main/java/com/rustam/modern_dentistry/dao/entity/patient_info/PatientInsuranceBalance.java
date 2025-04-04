package com.rustam.modern_dentistry.dao.entity.patient_info;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient_insurance_balance ")
public class PatientInsuranceBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    LocalDate date;
    BigDecimal amount;
    Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_insurance_id", nullable = false)
    PatientInsurance patientInsurance;
}
