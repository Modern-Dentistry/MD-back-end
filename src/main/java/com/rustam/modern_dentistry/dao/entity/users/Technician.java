package com.rustam.modern_dentistry.dao.entity.users;

import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "technician")
@FieldDefaults(level = PRIVATE)
public class Technician {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    String username;
    String password;
    String name;
    String surname;
    String fatherName;
    LocalDate birthDate;
    String phoneNumber1;
    String phoneNumber2;
    String homePhoneNumber;
    String address;
    @Column(unique = true)
    String finCode;
    @Enumerated(EnumType.STRING)
    GenderStatus gender;
    //    List<Permission> permissions
}
