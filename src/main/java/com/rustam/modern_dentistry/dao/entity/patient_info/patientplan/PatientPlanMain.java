package com.rustam.modern_dentistry.dao.entity.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.CoreEntity;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Table(name = "patient_plan_main")
@Entity
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class PatientPlanMain extends CoreEntity {

    String planName;

    String key;

    @OneToMany(mappedBy = "patientPlanMain", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PatientPlan> patientPlans;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    Patient patient;
}
