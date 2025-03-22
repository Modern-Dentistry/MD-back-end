package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.settings.PriceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceCategoryRepository extends JpaRepository<PriceCategory, Long> {
    List<PriceCategory> findByIdIn(List<Long> ids);
}
