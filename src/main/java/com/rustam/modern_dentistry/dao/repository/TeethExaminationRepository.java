package com.rustam.modern_dentistry.dao.repository;


import com.rustam.modern_dentistry.dao.entity.Examination;
import com.rustam.modern_dentistry.dao.entity.teeth.TeethExamination;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TeethExaminationRepository extends JpaRepository<TeethExamination,Long> , JpaSpecificationExecutor<TeethExamination> {
    boolean existsTeethExaminationByExamination_Id(Long examinationId);
    @EntityGraph(attributePaths = {"teeth", "examination"}) // Əlaqəli obyektləri əvvəlcədən yüklə
    List<TeethExamination> findAll();

    @EntityGraph(attributePaths = {"teeth"}) // Əlaqəli obyektləri əvvəlcədən yüklə
    List<TeethExamination> findAll(Specification<TeethExamination> spec);
}
