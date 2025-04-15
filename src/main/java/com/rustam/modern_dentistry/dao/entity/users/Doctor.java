package com.rustam.modern_dentistry.dao.entity.users;






import com.rustam.modern_dentistry.dao.entity.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rustam.modern_dentistry.dao.entity.GeneralCalendar;
import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import com.rustam.modern_dentistry.dao.entity.patient_info.PatientExaminations;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "doctors")
@PrimaryKeyJoinColumn(name = "doctor_id")
@DiscriminatorValue("DOCTOR")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends BaseUser {
    @OneToMany(mappedBy = "doctor", fetch = LAZY)
    @JsonIgnore
    Set<Patient> patients;
    @Column(name = "color_code")
    String colorCode;
    String degree;
    @Column(name = "phone_2")
    String phone2;
    @Column(name = "phone_3")
    String phone3;
    @Column(name = "home_phone")
    String homePhone;
    String address;
    Integer experience;

    @OneToMany(mappedBy = "doctor", cascade = ALL, fetch = LAZY)
    List<Reservation> reservations;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    Set<GeneralCalendar> generalCalendars;
}
