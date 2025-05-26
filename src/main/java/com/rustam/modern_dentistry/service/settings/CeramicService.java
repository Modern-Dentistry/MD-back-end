package com.rustam.modern_dentistry.service.settings;

import com.rustam.modern_dentistry.dao.entity.settings.Ceramic;
import com.rustam.modern_dentistry.dao.repository.settings.CeramicRepository;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CeramicService {
    private final CeramicRepository ceramicRepository;

    public Ceramic getCeramicById(Long id) {
        return ceramicRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də ceramic tapımadı: " + id)
        );
    }
}
