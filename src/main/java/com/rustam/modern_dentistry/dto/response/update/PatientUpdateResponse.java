package com.rustam.modern_dentistry.dto.response.update;

import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import com.rustam.modern_dentistry.dao.entity.enums.status.SpecializationStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientUpdateResponse {
    String name;
    String surname;
    String patronymic;
    String finCode;
    GenderStatus genderStatus;
    LocalDate dateOfBirth;
    String priceCategoryStatus;
    SpecializationStatus specializationStatus;
    String phone;
    String workPhone;
    String homePhone;
    String homeAddress;
    String workAddress;
    String email;
}
