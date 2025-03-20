package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.Examination;
import com.rustam.modern_dentistry.dao.entity.teeth.Teeth;
import com.rustam.modern_dentistry.dao.entity.teeth.TeethExamination;
import com.rustam.modern_dentistry.dao.repository.TeethExaminationRepository;
import com.rustam.modern_dentistry.dto.request.create.TeethExaminationRequest;
import com.rustam.modern_dentistry.dto.request.read.TeethExaminationSearchRequest;
import com.rustam.modern_dentistry.dto.response.create.TeethExaminationResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.mapper.TeethExaminationMapper;
import com.rustam.modern_dentistry.util.specification.TeethExaminationSpecification;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TeethExaminationService {

    TeethExaminationRepository teethExaminationRepository;
    ExaminationService examinationService;
    TeethService teethService;
    TeethExaminationMapper teethExaminationMapper;

    public TeethExaminationResponse create(TeethExaminationRequest teethExaminationRequest) {
        Teeth teeth = teethService.findById(teethExaminationRequest.getTeethId());
        Long examinationId = teethExaminationRequest.getExaminationIds().stream()
                .findFirst()
                .orElse(null);
        boolean existsTeethExaminationByExaminationId = teethExaminationRepository.existsTeethExaminationByExamination_Id(examinationId);
        if (existsTeethExaminationByExaminationId){
            throw new ExistsException("This examination for this tooth is now available.");
        }
        Examination examination = examinationService.findById(examinationId);
        TeethExamination teethExamination = TeethExamination.builder()
                .teeth(teeth)
                .examination(examination)
                .build();
        teethExaminationRepository.save(teethExamination);
        return teethExaminationMapper.toTeethExaminationResponse(teethExamination);
    }

    public List<TeethExaminationResponse> read() {
        List<TeethExamination> teethExaminations = teethExaminationRepository.findAll();
        return teethExaminationMapper.toTeethExaminationResponses(teethExaminations);
    }

    public List<TeethExaminationResponse> search(TeethExaminationSearchRequest teethExaminationSearchRequest) {
        List<TeethExamination> teethExaminations = teethExaminationRepository.findAll(TeethExaminationSpecification.filterBy(teethExaminationSearchRequest));
        return teethExaminationMapper.toTeethExaminationResponses(teethExaminations);
    }
}
