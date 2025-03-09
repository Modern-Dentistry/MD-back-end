package com.rustam.modern_dentistry.dao.entity.users;

import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Table(name = "receptions")
@PrimaryKeyJoinColumn(name = "reception_id")
@DiscriminatorValue("RECEPTION")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reception extends BaseUser {
    String patronymic;
    @Column(unique = true,name = "fin_code")
    String finCode;
    @Column(name = "gender_status")
    GenderStatus genderStatus;
    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;
    @Column(name = "home_phone")
    String homePhone;
    String address;
    Integer experience;
}
