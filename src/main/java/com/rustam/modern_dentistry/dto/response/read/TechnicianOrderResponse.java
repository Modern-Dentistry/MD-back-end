package com.rustam.modern_dentistry.dto.response.read;

import com.rustam.modern_dentistry.dao.entity.enums.DentalOrderStatus;
import com.rustam.modern_dentistry.dao.entity.enums.DentalOrderType;
import com.rustam.modern_dentistry.dao.entity.laboratory.OrderDentureInfo;
import jakarta.persistence.Embedded;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class TechnicianOrderResponse {
    Long id;
    LocalDate checkDate;
    LocalDate deliveryDate;
    String description;

    @Embedded
    OrderDentureInfo orderDentureInfo;

    DentalOrderType orderType;
    DentalOrderStatus orderStatus;

    List<DentalOrderToothDetailResponse> toothDetails;
    List<DentalOrderTeethListResponse> teethList;

    String doctor;
    String technician;
    String patient;

    List<String> urls;
}
