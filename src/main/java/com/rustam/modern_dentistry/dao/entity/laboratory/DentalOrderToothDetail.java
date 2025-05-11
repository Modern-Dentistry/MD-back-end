package com.rustam.modern_dentistry.dao.entity.laboratory;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dental_order_tooth_detail")
public class DentalOrderToothDetail {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    String color;
    String metal;
    String ceramic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dental_order_id", referencedColumnName = "id")
    DentalOrder dentalOrder;
}
