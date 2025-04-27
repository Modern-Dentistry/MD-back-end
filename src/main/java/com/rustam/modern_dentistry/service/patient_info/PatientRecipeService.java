package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.repository.PatientRecipeRepository;
import com.rustam.modern_dentistry.dto.request.create.PatRecipeCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatRecipeUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatRecipeReadRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientRecipeService {
    private final PatientRecipeRepository patientRecipeRepository;

    public void create(@Valid PatRecipeCreateReq req) {
    }

    public List<PatRecipeReadRes> read() {
        return  null;
    }

    public List<PatRecipeReadRes> readAllById(Long patientId) {
        return  null;
    }

    public void update(Long id, @Valid PatRecipeUpdateReq request) {
    }

    public void delete(Long id) {
    }
}
