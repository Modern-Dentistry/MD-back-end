package com.rustam.modern_dentistry.dao.entity.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.CoreEntity;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpType;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "patient_plan")
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class PatientPlan extends CoreEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_plan_main_id")
    PatientPlanMain patientPlanMain;

    @Column(name = "tooth_id")
    Long toothId;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "operation_type_id")
    OpType opType;

    @OneToMany(mappedBy = "patientPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PatientPlanPartOfToothDetail> details;

    @Column(name = "is_completed")
    boolean isCompleted;
}
