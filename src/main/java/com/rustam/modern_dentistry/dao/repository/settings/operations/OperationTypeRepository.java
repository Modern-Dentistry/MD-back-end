package com.rustam.modern_dentistry.dao.repository.settings.operations;

import com.rustam.modern_dentistry.dto.projection.OperationCategoryProjection;
import com.rustam.modern_dentistry.dto.response.read.OpInsuranceReadResponse;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OperationTypeRepository extends JpaRepository<OpType, Long>, JpaSpecificationExecutor<OpType> {

    @EntityGraph(attributePaths = {"insurances", "opTypeItems"})
    Optional<OpType> findById(Long id);

    @EntityGraph(attributePaths = {"opTypeItems"})
    List<OpType> findAll();

    @EntityGraph(attributePaths = {"opTypeItems"})
    Page<OpType> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"opTypeItems"})
    Page<OpType> findAll(Specification<OpType> spec, Pageable pageable);


    @Query("""
            select new com.rustam.modern_dentistry.dto.response.read.OpInsuranceReadResponse(
                d.id as insuranceCompanyId,
                d.companyName, 
                i.deductiblePercentage
            ) 
            from InsuranceCompany d 
            left join OpTypeInsurance i ON i.insuranceCompany.id = d.id AND (i.opType.id = :opTypeId OR i.opType.id IS NULL)
            """)
    List<OpInsuranceReadResponse> findByOpTypeId(@Param("opTypeId") Long opTypeId);

    Optional<OpType> findByCategoryName(String operationCategoryName);

    @Query(value = """
    SELECT
        ot.id as categoryId,
        ot.category_name as categoryName,
        ot.category_code as categoryCode,
        COALESCE(opii.id, opi.id) as operationId ,
        COALESCE(opii.name, opi.operation_name) as name,
        COALESCE(opii.amount, opi.amount) as amount,
        COALESCE(opii.specific_code, opi.operation_code) as operationCode,
        opi.status as status
    FROM op_types ot
    INNER JOIN op_type_items opi ON ot.id = opi.op_type_id
    LEFT JOIN op_type_item_insurances opii 
        ON opi.id = opii.op_type_item_id 
        AND opii.insurance_id = :insuranceId
    WHERE
        (:insuranceId IS NULL OR opii.id IS NOT NULL)
    """, nativeQuery = true)
    List<OperationCategoryProjection> findAllByInsuranceToCategoryOfOperations(Long insuranceId);
}
