package com.rustam.modern_dentistry.dao.entity.settings.operations;

import com.rustam.modern_dentistry.dao.entity.settings.PriceCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "op_type_item_prices")
@FieldDefaults(level = PRIVATE)
public class OpTypeItemPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_type_id", nullable = false)
    PriceCategory priceCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "op_type_item_id", nullable = false, unique = true)
    OpTypeItem opTypeItem;
}