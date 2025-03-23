package com.rustam.modern_dentistry.dao.repository;


import com.rustam.modern_dentistry.dao.entity.teeth.TeethOperation;
import com.rustam.modern_dentistry.dto.response.read.TeethOperationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeethOperationRepository extends JpaRepository<TeethOperation,Long> {

    @Query("SELECT new com.rustam.modern_dentistry.dto.response.read.TeethOperationResponse(t.id, i.operationName) " +
            "FROM TeethOperation t " +
            "JOIN t.opTypeItem i")
    List<TeethOperationResponse> findAllTeethOperations();
}
