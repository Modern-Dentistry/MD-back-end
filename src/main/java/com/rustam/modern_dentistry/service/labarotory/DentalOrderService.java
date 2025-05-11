package com.rustam.modern_dentistry.service.labarotory;

import com.rustam.modern_dentistry.dao.repository.laboratory.DentalOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DentalOrderService {
    private final DentalOrderRepository dentalOrderRepository;

}
