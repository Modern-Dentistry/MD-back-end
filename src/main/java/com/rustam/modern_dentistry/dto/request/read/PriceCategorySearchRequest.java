package com.rustam.modern_dentistry.dto.request.read;

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
public class PriceCategorySearchRequest {
    String name;
    Status status;
}
