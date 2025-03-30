package com.rustam.modern_dentistry.service.settings.anemnesis;

import com.rustam.modern_dentistry.dao.repository.settings.anemnesis.AnemnesisCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnemnesisListService {
    private final AnemnesisCategoryRepository anemnesisCategoryRepository;
}
