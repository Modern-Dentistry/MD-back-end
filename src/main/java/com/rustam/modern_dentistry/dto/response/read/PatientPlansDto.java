package com.rustam.modern_dentistry.dto.response.read;


import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanPartOfToothDetail;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PatientPlansDto {

    Long toothNo;
    OperationOfCategoryDto operationOfCategoryDto;
    List<PatientPlanPartOfToothDetailDto> partOfTeethIds;
}
