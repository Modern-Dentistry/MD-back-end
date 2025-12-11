package com.rustam.modern_dentistry.dto.projection;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;

import java.math.BigDecimal;

public interface OperationCategoryProjection {
    String getCategoryName();
    String getCategoryCode();
    String getName();
    BigDecimal getAmount();
    String getOperationCode();
    Status getStatus();
    Long getOperationId();
    Long getCategoryId();

}
