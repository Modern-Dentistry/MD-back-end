package com.rustam.modern_dentistry.dto.response.read;

import com.rustam.modern_dentistry.dao.entity.enums.status.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReservationReadResponse {
    Long id;
    String mobilePhone;
    LocalDate startDate;
    LocalDate endDate;
    LocalTime startTime;
    LocalTime endTime;
    String doctorName;
    String doctorSurname;
    String patientName;
    String patientSurname;
    ReservationStatus status;
}
