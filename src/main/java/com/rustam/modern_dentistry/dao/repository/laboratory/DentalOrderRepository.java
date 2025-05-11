package com.rustam.modern_dentistry.dao.repository.laboratory;

import com.rustam.modern_dentistry.dao.entity.laboratory.DentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentalOrderRepository extends JpaRepository<DentalOrder, Long> {
}
