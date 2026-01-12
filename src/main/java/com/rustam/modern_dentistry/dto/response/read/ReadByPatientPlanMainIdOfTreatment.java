package com.rustam.modern_dentistry.dto.response.read;

import com.rustam.modern_dentistry.dto.response.create.PatientPlansResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadByPatientPlanMainIdOfTreatment {
    String key;
    UUID patientPlanMainId;
    Boolean isSave;
    List<PlanDetailDto> plans;
}
