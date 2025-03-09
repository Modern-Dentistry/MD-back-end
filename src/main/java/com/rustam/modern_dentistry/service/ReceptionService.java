package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.users.Reception;
import com.rustam.modern_dentistry.dao.repository.ReceptionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReceptionService {

    ReceptionRepository receptionRepository;

    public List<Reception> findAll(){
        return receptionRepository.findAll();
    }
}
