package com.rustam.modern_dentistry.dto.request.create;

import com.rustam.modern_dentistry.dao.entity.enums.status.Appointment;
import com.rustam.modern_dentistry.dao.entity.enums.status.Room;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewAppointmentRequest {
    @NotNull
    UUID doctorId;
    @NotNull
    Room room;
    @NotBlank(message = "patient bos ola bilmez")
    Long patientId;

    Appointment appointment;

    LocalDate date;

    LocalTime time;

    LocalTime period;
}
