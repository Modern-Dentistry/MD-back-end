package com.rustam.modern_dentistry.dao.repository.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderFromWarehouseRepository extends JpaRepository<OrderFromWarehouse,Long>, JpaSpecificationExecutor<OrderFromWarehouse> {
}
