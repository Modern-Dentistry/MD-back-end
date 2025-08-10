package com.rustam.modern_dentistry.dto.request.read;

import com.rustam.modern_dentistry.dao.entity.enums.status.Room;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoomStockRequest {

    String cabinetName;
    String categoryName;
    String productName;
    Long productNo;
}
