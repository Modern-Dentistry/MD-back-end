package com.rustam.modern_dentistry.dao.entity.settings;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "insurance_companies")
@FieldDefaults(level = PRIVATE)
public class InsuranceCompany {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false)
    String companyName;
    BigDecimal deductibleAmount;

    @Enumerated(STRING)
    Status status;

    public Long getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public BigDecimal getDeductibleAmount() {
        return deductibleAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDeductibleAmount(BigDecimal deductibleAmount) {
        this.deductibleAmount = deductibleAmount;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
