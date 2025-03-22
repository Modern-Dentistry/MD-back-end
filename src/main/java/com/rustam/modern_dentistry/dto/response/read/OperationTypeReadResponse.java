package com.rustam.modern_dentistry.dto.response.read;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dto.request.create.OpTypeInsuranceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class OperationTypeReadResponse {
    Long id;
    String categoryName;
    boolean colorSelection;
    boolean implantSelection;
    Status status;
    Long opTypeItemCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<InsDeducReadResponse> insurances;
}
