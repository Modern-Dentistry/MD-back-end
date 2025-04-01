package com.rustam.modern_dentistry.dao.entity.patient_info;

import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

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
    String doctorId;

    @Column(name = "patient_appointment_date")
    LocalDate patientAppointmentDate;
}
