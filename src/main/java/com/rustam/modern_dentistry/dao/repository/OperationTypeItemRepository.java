package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import com.rustam.modern_dentistry.dto.response.read.*;
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

public interface OperationTypeItemRepository extends JpaRepository<OpTypeItem, Long>, JpaSpecificationExecutor<OpTypeItem> {
    @EntityGraph(attributePaths = {"prices"})
    Page<OpTypeItem> findAll(Specification<OpTypeItem> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"prices", "insurances"})
    Optional<OpTypeItem> findById(Long id);

    @Query("""
        select new com.rustam.modern_dentistry.dto.response.read.InsItemDeducReadResponse(
            d.id as insuranceId,
            d.companyName, 
            i.name, 
            i.amount
        ) 
        from InsuranceCompany d 
        left join OpTypeItemInsurance i 
        ON i.insuranceCompany.id = d.id 
        AND (i.opTypeItem.id = :opTypeItemId OR i.opTypeItem IS NULL)
        """)
    List<InsItemDeducReadResponse> findInsurancesByOpTypeItemId(@Param("opTypeItemId") Long opTypeItemId);

    @Query("""
        select new com.rustam.modern_dentistry.dto.response.read.OpTypeItemPricesDto(
            d.id as priceCategoryId,
            i.price
        ) 
        from PriceCategory d 
        left join OpTypeItemPrice i 
        ON i.priceCategory.id = d.id 
        AND (i.opTypeItem.id = :opTypeItemId OR i.opTypeItem IS NULL)
        """)
    List<OpTypeItemPricesDto> findPricesByOpTypeItemId(@Param("opTypeItemId") Long opTypeItemId);
}
