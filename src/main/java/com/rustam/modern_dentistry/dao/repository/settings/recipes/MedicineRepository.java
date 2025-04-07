package com.rustam.modern_dentistry.dao.repository.settings.recipes;

import com.rustam.modern_dentistry.dao.entity.settings.recipes.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
