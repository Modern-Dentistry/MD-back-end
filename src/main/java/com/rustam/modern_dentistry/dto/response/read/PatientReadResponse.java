package com.rustam.modern_dentistry.dto.response.read;

import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import com.rustam.modern_dentistry.dao.entity.enums.status.SpecializationStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientReadResponse {

    Long id;
    String name;
    String surname;
    String patronymic;
    String finCode;
    GenderStatus genderStatus;
    LocalDate dateOfBirth;
    String priceCategoryName;
    String specializationCategoryName;
    String baseUser;
    String phone;
    String workPhone;
    String homePhone;
    String homeAddress;
    String workAddress;
    String email;
    Boolean isBlocked;
}
