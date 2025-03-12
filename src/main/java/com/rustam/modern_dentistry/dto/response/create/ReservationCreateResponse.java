package com.rustam.modern_dentistry.dto.response.create;

import com.rustam.modern_dentistry.dao.entity.enums.WeekDay;
import com.rustam.modern_dentistry.dao.entity.enums.status.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReservationCreateResponse {
    Long id;
    LocalDate startDate;
    LocalDate endDate;
    LocalTime startTime;
    LocalTime endTime;
    Set<WeekDay> weekDays;
    String doctorId;
    Long patientId;
    ReservationStatus status;
}
