package com.rustam.modern_dentistry.dto.response.create;

import com.rustam.modern_dentistry.dao.entity.enums.status.Appointment;
import com.rustam.modern_dentistry.dto.response.read.AppointmentTypeResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewAppointmentResponse {
    String doctorName;
    String cabinetName;

    String patientName;

    Appointment appointment;

    LocalDate date;

    List<AppointmentTypeResponse> appointmentTypeResponses;

    LocalTime time;

    LocalTime period;
}
