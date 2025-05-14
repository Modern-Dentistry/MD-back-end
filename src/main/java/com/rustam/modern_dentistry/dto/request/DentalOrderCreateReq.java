package com.rustam.modern_dentistry.dto.request;

import com.rustam.modern_dentistry.dao.entity.laboratory.OrderDentureInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DentalOrderCreateReq {
    LocalDate checkDate;
    LocalDate deliveryDate;
    String description;

    String orderType;
    String orderStatus;

    OrderDentureInfo orderDentureInfo;

    List<Long> toothDetailIds;
    List<Long> teethList;

    Long doctorId;
    Long technicianId;
    Long patientId;

    List<String> imagePaths;
}
