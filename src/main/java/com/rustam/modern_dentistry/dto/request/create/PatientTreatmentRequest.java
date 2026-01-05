package com.rustam.modern_dentistry.dto.request.create;

import com.rustam.modern_dentistry.dto.response.read.PatientPlansDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientTreatmentRequest {
    
    UUID planMainId;
    List<PatientPlansRequest> patientPlansRequests;

}
