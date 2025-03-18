package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OperationTypeRepository extends JpaRepository<OperationType, Long>, JpaSpecificationExecutor<OperationType> {
}
