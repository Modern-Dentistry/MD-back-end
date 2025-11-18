package com.rustam.modern_dentistry.dto.request.save;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientPlansSaveRequest {

    UUID patientPlanMainId;
}
