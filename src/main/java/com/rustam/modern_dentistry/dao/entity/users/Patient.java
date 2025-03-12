package com.rustam.modern_dentistry.dao.entity.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rustam.modern_dentistry.dao.entity.GeneralCalendar;
import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import com.rustam.modern_dentistry.dao.entity.enums.status.PriceCategoryStatus;
import com.rustam.modern_dentistry.dao.entity.enums.status.SpecializationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String surname;
    String patronymic;
    @Column(unique = true,name = "fin_code")
    String finCode;
    @Column(name = "gender_status")
    GenderStatus genderStatus;
    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    Doctor doctor;
    @Column(name = "price_category_status")
    PriceCategoryStatus priceCategoryStatus;
    @Column(name = "specialication_status")
    SpecializationStatus specializationStatus;
    String phone;
    String email;
    Boolean enabled;
    @Column(name = "work_phone")
    String workPhone;
    @Column(name = "home_phone")
    String homePhone;
    @Column(name = "home_address")
    String homeAddress;
    @Column(name = "work_address")
    String workAddress;
    LocalDate registration_date;
    String role;


    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<GeneralCalendar> generalCalendars;
}
