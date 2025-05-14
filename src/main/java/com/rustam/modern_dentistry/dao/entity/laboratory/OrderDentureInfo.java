package com.rustam.modern_dentistry.dao.entity.laboratory;

import jakarta.persistence.Embeddable;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Embeddable
@FieldDefaults(level = PRIVATE)
public class OrderDentureInfo {
    String color;
    String garniture;
}
