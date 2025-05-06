package com.rustam.modern_dentistry.dto.response.read;

import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TechnicianReadResponse {
    Long id;
    String username;
    String name;
    String surname;
    String fatherName;
    LocalDate birthDate;
    String phoneNumber1;
    String phoneNumber2;
    String homePhoneNumber;
    String address;
    String finCode;
    GenderStatus gender;
}
