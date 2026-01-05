package com.rustam.modern_dentistry.dto.request.create;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientExaminationsCreateRequest {

    Long patientId;
    Long toothId;
    List<Long> partOfTeethIds;
    Long examinationId;
}
