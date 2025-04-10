package com.rustam.modern_dentistry.dao.repository.warehouse_entry;

import com.rustam.modern_dentistry.dao.entity.warehouse_entry.WarehouseEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WarehouseEntryRepository extends JpaRepository<WarehouseEntry,Long>, JpaSpecificationExecutor<WarehouseEntry> {
}
