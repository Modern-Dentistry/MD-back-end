package com.rustam.modern_dentistry.dto.request.update;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientPlanUpdateRequest {

    UUID patientPlanMainId;
    UUID id;
    Long toothId;
    Long categoryId;
    Long operationId;
    List<Long> partOfTeethIds;
}
