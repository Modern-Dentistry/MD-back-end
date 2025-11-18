package com.rustam.modern_dentistry.dto.response.read;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static jakarta.persistence.FetchType.LAZY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PatientPlanPartOfToothDetailDto {

    Long id;

    Long partOfToothId;

    String operationName;
}
