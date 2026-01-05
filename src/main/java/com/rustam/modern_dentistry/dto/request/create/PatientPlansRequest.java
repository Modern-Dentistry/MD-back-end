package com.rustam.modern_dentistry.dto.request.create;

import com.rustam.modern_dentistry.dto.response.read.OperationOfCategoryDto;
import com.rustam.modern_dentistry.dto.response.read.PatientPlanPartOfToothDetailDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientPlansRequest {

    UUID planId;
    Boolean isChecked;
    //List<PatientTreatmentDetailsRequest> requests;
}
