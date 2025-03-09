package com.rustam.modern_dentistry.dto.request.create;

import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import com.rustam.modern_dentistry.dao.entity.enums.status.PriceCategoryStatus;
import com.rustam.modern_dentistry.dao.entity.enums.status.SpecializationStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddWorkerCreateRequest {
    String username;
    String password;
    String name;
    String surname;
    String patronymic;
    String finCode;
    String colorCode;
    GenderStatus genderStatus;
    LocalDate dateOfBirth;
    String degree;
    String phone;
    String phone2;
    String homePhone;
    String email;
    String address;
    String workAddress;
    Integer experience;
    Set<Role> authorities;
}
