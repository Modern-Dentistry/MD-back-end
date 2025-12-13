package com.rustam.modern_dentistry.dao.repository.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dto.request.save.PatientPlansSaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientPlanMainRepository extends JpaRepository<PatientPlanMain,UUID> {
    boolean existsByPlanName(String planName);

    List<PatientPlanMain> findAllByStatusAndActionStatus(String a, String a1);

    @Query("SELECT ppm FROM PatientPlanMain ppm " +
            "LEFT JOIN FETCH ppm.patientPlans " +
            "WHERE ppm.id = :id " +
            "AND ppm.status IN :statuses " +
            "AND ppm.actionStatus IN :actionStatuses")
    Optional<PatientPlanMain> findByIdAndStatusInAndActionStatusIn(UUID id, List<String> statuses, List<String> actionStatuses);

}
