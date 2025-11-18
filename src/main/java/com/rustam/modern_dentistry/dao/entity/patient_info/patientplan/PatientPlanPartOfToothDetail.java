package com.rustam.modern_dentistry.dao.entity.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.FetchType.LAZY;

@Table(name = "patient_plan_part_of_tooth_detail")
@Entity
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class PatientPlanPartOfToothDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "patient_plan_id")
    PatientPlan patientPlan;

    @Column(name = "part_of_tooth_id")
    Long partOfToothId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "operation_type_item_id")
    OpTypeItem opTypeItem;
}
