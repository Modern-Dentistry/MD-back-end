package com.rustam.modern_dentistry.dto.request.create;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientPlansCreateRequest {

    UUID patientPlanMainId;
    Long toothId;
    Long categoryId;
    Long operationId;
    List<Long> partOfTeethIds;
}
