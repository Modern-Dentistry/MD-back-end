package com.rustam.modern_dentistry.service.labarotory;

import com.rustam.modern_dentistry.dao.repository.laboratory.DentalOrderRepository;
import com.rustam.modern_dentistry.dto.request.DentalOrderCreateReq;
import com.rustam.modern_dentistry.mapper.laboratory.DentalOrderMapper;
import com.rustam.modern_dentistry.service.settings.teeth.TeethService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DentalOrderService {
    private final TeethService teethService;
    private final DentalOrderMapper dentalOrderMapper;
    private final DentalOrderRepository dentalOrderRepository;

    public void create(DentalOrderCreateReq request) {
        var entity = dentalOrderMapper.toEntity(request);
        var teeth = teethService.findAllById(request.getTeethList());
        entity.setTeethList(teeth);
        dentalOrderRepository.save(entity);
    }
}
