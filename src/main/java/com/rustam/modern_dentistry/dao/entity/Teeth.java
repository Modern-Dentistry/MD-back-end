package com.rustam.modern_dentistry.dao.entity;

import com.rustam.modern_dentistry.dao.entity.enums.status.ToothLocation;
import com.rustam.modern_dentistry.dao.entity.enums.status.ToothType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "teeth")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Teeth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long toothNo;

    ToothType toothType;

    ToothLocation toothLocation;

    Examination examination;
}
