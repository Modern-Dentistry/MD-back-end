package com.rustam.modern_dentistry.dto.response.read;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlanDetailDto {
    UUID patientPlanId;
    Long categoryId;
    String categoryName;
    String categoryCode;
    Long toothNo;
    List<PatientPlansOfTreatmentResponse> details;
}
