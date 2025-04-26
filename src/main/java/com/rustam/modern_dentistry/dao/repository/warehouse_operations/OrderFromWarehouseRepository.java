package com.rustam.modern_dentistry.dao.repository.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.warehouse_operations.OrderFromWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderFromWarehouseRepository extends JpaRepository<OrderFromWarehouse,Long>, JpaSpecificationExecutor<OrderFromWarehouse> {

//    @Query("SELECT o FROM OrderFromWarehouse o LEFT JOIN FETCH o.orderFromWarehouseProducts WHERE o.id = :id")
//    Optional<OrderFromWarehouse> findByIdWithProducts(Long id);
}
