package com.rustam.modern_dentistry.dao.entity.warehouse_operations;


import com.rustam.modern_dentistry.dao.entity.enums.status.PendingStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "warehouse_removal_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseRemovalProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long categoryId;
    Long productId;
    Long sendQuantity;

    String productName;
    String categoryName;
    String productDescription;

    @Enumerated(EnumType.STRING)
    PendingStatus pendingStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_removal_id")
    WarehouseRemoval warehouseRemoval;
}
