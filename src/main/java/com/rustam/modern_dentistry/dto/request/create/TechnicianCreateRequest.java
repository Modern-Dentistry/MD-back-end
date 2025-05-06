package com.rustam.modern_dentistry.dto.request.create;

import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.rustam.modern_dentistry.util.constants.ValidationErrorMessage.*;

@Getter
@Setter
public class TechnicianCreateRequest {
    @NotBlank(message = VALIDATION_USERNAME)
    String username;
    @NotBlank(message = VALIDATION_PASSWORD)
    String password;
    @NotBlank(message = VALIDATION_NAME)
    String name;
    @NotBlank(message = VALIDATION_SURNAME)
    String surname;
    @NotBlank(message = VALIDATION_FIN_CODE)
    String finCode;
    @NotBlank(message = VALIDATION_DATE)
    LocalDate birthDate;
    @NotBlank(message = VALIDATION_MOBILE)
    String phoneNumber1;
    @NotNull(message = VALIDATION_GENDER)
    GenderStatus gender;
    String phoneNumber2;
    String homePhoneNumber;
    String fatherName;
    String address;
}
