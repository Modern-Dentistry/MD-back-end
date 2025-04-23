package com.rustam.modern_dentistry.dto.response.read;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatPhotosReadRes {
    Long id;
    LocalDate date;
    String description;
    Long patientId;
    String url;
}
