package com.rustam.modern_dentistry.dao.entity.patient_info;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patient_examinations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientExaminations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String diagnosis;

    @Column(name = "tooth_number")
    Long toothNumber;

    @Column(name = "patient_id")
    Long patientId;

    @Column(name = "doctor_id")
    UUID doctorId;

    @Column(name = "patient_appointment_date")
    LocalDate patientAppointmentDate;
}
