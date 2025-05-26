package com.rustam.modern_dentistry.service.settings;

import com.rustam.modern_dentistry.dao.repository.settings.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;
}
