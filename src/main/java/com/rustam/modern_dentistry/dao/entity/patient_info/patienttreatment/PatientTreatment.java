package com.rustam.modern_dentistry.dao.entity.patient_info.patienttreatment;


import com.rustam.modern_dentistry.dao.entity.CoreEntity;
import com.rustam.modern_dentistry.dao.entity.enums.status.ExecutedStatus;
import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "patient_treatments")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class PatientTreatment extends CoreEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_plan_main_id", nullable = false)
    PatientPlanMain patientPlanMain;

    @ManyToMany
    @JoinTable(
            name = "treatment_executed_plans",
            joinColumns = @JoinColumn(name = "treatment_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_plan_id")
    )
    List<PatientPlan> executedPlans;

    @Column(name = "is_checked")
    boolean isChecked;

    @Column(name = "actual_price")
    BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    ExecutedStatus executedStatus;

    @Column(name = "notes", columnDefinition = "TEXT")
    String notes;
}

