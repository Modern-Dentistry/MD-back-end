package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTypeItemRepository extends JpaRepository<OpTypeItem, Long> {
}
