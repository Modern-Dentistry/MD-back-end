package com.rustam.modern_dentistry.dao.repository.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dto.request.create.PatientPlansCreateRequest;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientPlansRepository extends JpaRepository<PatientPlan, UUID> {

    Optional<PatientPlan> findByIdAndPatientPlanMainId(UUID patientPlanId, UUID patientPlanMainId);

    List<PatientPlan> findAllByActionStatusAndStatus(String a, String a1);

    @Query("""
        SELECT CASE WHEN COUNT(pp) > 0 THEN true ELSE false END
        FROM PatientPlan pp
        JOIN pp.details d
        WHERE pp.patientPlanMain.id = :#{#patientPlanMainId}
        AND pp.toothId = :#{#toothId}
        AND pp.opType.id = :#{#categoryId}
        AND d.partOfToothId IN :#{#partOfTeethIds}
        AND d.opTypeItem.id = :#{#operationId}
    """)
    boolean existsByToothAndOperations(UUID patientPlanMainId,Long toothId,Long categoryId,List<Long> partOfTeethIds,Long operationId);

    List<PatientPlan> findAllByActionStatusInAndStatusIn(List<String> a, List<String> a1);

    Optional<PatientPlan> findByIdAndStatusInAndActionStatusIn(UUID id, List<String> a, List<String> a1);
}
