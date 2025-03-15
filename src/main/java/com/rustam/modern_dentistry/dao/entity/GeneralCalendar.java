package com.rustam.modern_dentistry.dao.entity;

import com.rustam.modern_dentistry.dao.entity.enums.status.Appointment;
import com.rustam.modern_dentistry.dao.entity.enums.status.Room;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "general_calendar")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneralCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    Doctor doctor;

    Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    Patient patient;

    @Enumerated(EnumType.STRING)
    Appointment appointment;

    LocalDate date;

    LocalTime time;

    LocalTime period;
}
