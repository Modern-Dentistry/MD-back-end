package com.rustam.modern_dentistry.dto.request.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class InsuranceCreateRequest {
    @NotBlank(message = "Şirkətin adını daxil edin.")
    String companyName;
    BigDecimal deductibleAmount;
}
