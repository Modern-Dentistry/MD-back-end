package com.rustam.modern_dentistry.dto.request.create;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientTreatmentDetailsRequest {

    Long partOfToothId;

    String operationName;

    BigDecimal price;
}
