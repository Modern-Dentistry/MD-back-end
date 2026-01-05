package com.rustam.modern_dentistry.dto.request.create;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientExaminationsUpdateRequest {

    Long id;
    Long patientId;
    Long toothNumber;
    List<Long> partOfTeethIds;
    Long examinationId;
}
