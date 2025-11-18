package com.rustam.modern_dentistry.dto.response.read;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryOfOperationDto {

    Long id;
    String categoryName;
    String categoryCode;
}
