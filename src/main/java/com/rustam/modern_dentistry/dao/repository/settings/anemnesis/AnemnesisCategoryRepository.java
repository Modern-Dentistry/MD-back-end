package com.rustam.modern_dentistry.dao.repository.settings.anemnesis;

import com.rustam.modern_dentistry.dao.entity.settings.anamnesis.AnamnesisCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnemnesisCategoryRepository extends JpaRepository<AnamnesisCategory, Long>, JpaSpecificationExecutor<AnamnesisCategory> {
}
