package com.rustam.modern_dentistry.dto.response.create;

import com.rustam.modern_dentistry.dto.response.read.PatientPlansDto;
import com.rustam.modern_dentistry.dto.response.read.PatientPlansMainResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientPlansResponse {

    UUID id;
    PatientPlansDto patientPlansDto;

}
