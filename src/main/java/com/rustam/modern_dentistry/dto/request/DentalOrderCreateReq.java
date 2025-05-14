package com.rustam.modern_dentistry.dto.request;

import com.rustam.modern_dentistry.dao.entity.laboratory.DentalOrderToothDetail;
import com.rustam.modern_dentistry.dao.entity.laboratory.OrderDentureInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class DentalOrderCreateReq {
    LocalDate checkDate;
    LocalDate deliveryDate;
    String description;

    String orderType;
    String orderStatus;

    OrderDentureInfo orderDentureInfo;

    List<DentalOrderToothDetail> toothDetailIds;
    List<Long> teethList;

    UUID doctorId;
    UUID technicianId;
    Long patientId;
}
