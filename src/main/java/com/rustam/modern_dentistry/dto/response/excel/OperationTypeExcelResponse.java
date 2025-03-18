package com.rustam.modern_dentistry.dto.response.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class OperationTypeExcelResponse {
    @JsonProperty("Əməliyyat kateqoriyasının adı")
    String categoryName;

    @JsonProperty("Status")
    Status status;
}
