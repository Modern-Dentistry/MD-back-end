package com.rustam.modern_dentistry.dao.entity.patient_info;

import com.rustam.modern_dentistry.dao.entity.users.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    Long toothNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    Patient patient;
}
