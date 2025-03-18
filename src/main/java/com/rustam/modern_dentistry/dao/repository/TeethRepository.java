package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.Teeth;
import com.rustam.modern_dentistry.dto.response.read.TeethResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeethRepository extends JpaRepository<Teeth,Long>, JpaSpecificationExecutor<Teeth> {
    Optional<Teeth> findByToothNo(Long toothNo);

    boolean existsTeethByToothNo(Long tootNo);

    @EntityGraph(attributePaths = "examinations")
    List<Teeth> findAll();

    @Query("SELECT t FROM Teeth t LEFT JOIN FETCH t.examinations")
    List<Teeth> findAllWithExaminations();
}
