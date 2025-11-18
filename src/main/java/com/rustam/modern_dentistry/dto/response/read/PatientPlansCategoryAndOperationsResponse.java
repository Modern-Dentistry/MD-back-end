package com.rustam.modern_dentistry.dto.response.read;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientPlansCategoryAndOperationsResponse {

    List<CategoryOfOperationDto> categoryOfOperationDtos;
    List<OpTypeItemReadResponse> opTypeItemReadResponses;
}
