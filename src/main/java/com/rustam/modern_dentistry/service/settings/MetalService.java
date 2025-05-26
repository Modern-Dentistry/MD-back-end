package com.rustam.modern_dentistry.service.settings;

import com.rustam.modern_dentistry.dao.entity.settings.Metal;
import com.rustam.modern_dentistry.dao.repository.settings.MetalRepository;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetalService {
    private final MetalRepository metalRepository;

    public Metal getMetalById(Long id) {
        return metalRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də metal tapımadı: " + id)
        );
    }
}
