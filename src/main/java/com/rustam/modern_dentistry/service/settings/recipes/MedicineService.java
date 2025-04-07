package com.rustam.modern_dentistry.service.settings.recipes;

import com.rustam.modern_dentistry.dao.repository.settings.recipes.MedicineRepository;
import com.rustam.modern_dentistry.mapper.settings.recipes.MedicineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicineService {
    private final MedicineMapper medicineMapper;
    private final MedicineRepository medicineRepository;
}
