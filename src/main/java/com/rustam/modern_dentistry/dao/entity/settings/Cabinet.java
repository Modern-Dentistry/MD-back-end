package com.rustam.modern_dentistry.dao.entity.settings;

import com.rustam.modern_dentistry.dao.entity.GeneralCalendar;
import com.rustam.modern_dentistry.dao.entity.WorkersWorkSchedule;
import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouse;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseReceipts;
import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseRemoval;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "cabinets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cabinet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String cabinetName;

    Status status;

    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, orphanRemoval = true)
    List<GeneralCalendar> generalCalendars;

    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderFromWarehouse> orderFromWarehouses;

    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, orphanRemoval = true)
    List<WarehouseReceipts> warehouseReceipts;

    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, orphanRemoval = true)
    List<WarehouseRemoval> warehouseRemovals;

    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, orphanRemoval = true)
    List<WorkersWorkSchedule> workersWorkSchedules;

}

