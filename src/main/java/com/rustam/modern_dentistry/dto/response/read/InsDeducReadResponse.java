package com.rustam.modern_dentistry.dto.response.read;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class InsDeducReadResponse {
    Long insuranceId;
    String companyName;
    BigDecimal deductibleAmount;
}
