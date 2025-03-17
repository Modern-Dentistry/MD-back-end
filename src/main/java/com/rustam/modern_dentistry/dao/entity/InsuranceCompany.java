package com.rustam.modern_dentistry.dao.entity;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "insurance_companies")
@FieldDefaults(level = PRIVATE)
public class InsuranceCompany {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    String companyName;
    BigDecimal deductibleAmount;

    @Enumerated(STRING)
    Status status;
}
