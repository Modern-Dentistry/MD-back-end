package com.rustam.modern_dentistry.dao.repository.settings;

import com.rustam.modern_dentistry.dao.entity.settings.SpecializationCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecializationCategoryRepository extends JpaRepository<SpecializationCategory, Long> {
    Optional<SpecializationCategory> findByName(String specializationCategoryName);

    boolean existsByName(String name);
}
