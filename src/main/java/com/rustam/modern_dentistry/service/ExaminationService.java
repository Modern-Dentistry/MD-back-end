package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.Examination;
import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.repository.ExaminationRepository;
import com.rustam.modern_dentistry.dto.request.create.CreateExaminationRequest;
import com.rustam.modern_dentistry.dto.request.create.ExaminationUpdateRequest;
import com.rustam.modern_dentistry.dto.request.read.ExaminationRequest;
import com.rustam.modern_dentistry.dto.response.read.ExaminationResponse;
import com.rustam.modern_dentistry.exception.custom.ExaminationNotFoundException;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.mapper.ExaminationMapper;
import com.rustam.modern_dentistry.util.specification.ExaminationSpecification;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ExaminationService {

    ExaminationRepository examinationRepository;
    ExaminationMapper examinationMapper;

    public void createExamination(CreateExaminationRequest createExaminationRequest) {
        boolean existsExaminationByTypeName = examinationRepository.existsExaminationByTypeName(createExaminationRequest.getExaminationTypeName());
        if (existsExaminationByTypeName){
            throw new ExistsException("There is such an examination.");
        }
        Examination examination = Examination.builder()
                .typeName(createExaminationRequest.getExaminationTypeName())
                .status(Status.ACTIVE)
                .build();
        examinationRepository.save(examination);
    }


    public List<ExaminationResponse> read() {
        List<Examination> examinations = examinationRepository.findAll();
        return examinationMapper.toDtos(examinations);
    }

    public List<ExaminationResponse> search(ExaminationRequest examinationRequest) {
        List<Examination> examinations = examinationRepository.findAll(ExaminationSpecification.filterBy(examinationRequest.getTypeName(), examinationRequest.getStatus()));
        return examinationMapper.toDtos(examinations);
    }

    public ExaminationResponse updateStatus(ExaminationRequest examinationRequest) {
        Examination examination = examinationRepository.findByTypeName(examinationRequest.getTypeName())
                .orElseThrow(() -> new ExaminationNotFoundException("examination not found"));
        if (examinationRequest.getTypeName() != null){
            examination.setTypeName(examinationRequest.getTypeName());
        }
        if (examinationRequest.getStatus() != null){
            examination.setStatus(examinationRequest.getStatus());
        }
        examinationRepository.save(examination);
        return examinationMapper.toDto(examination);
    }

    public Examination findById(Long id) {
        return examinationRepository.findById(id)
                .orElseThrow(() -> new ExaminationNotFoundException("examination not found"));
    }

    public void delete(Long id) {
        Examination examination = findById(id);
        examinationRepository.delete(examination);
    }

    public ExaminationResponse update(ExaminationUpdateRequest examinationUpdateRequest) {
        Examination examination = findById(examinationUpdateRequest.getId());
        if (examinationUpdateRequest.getTypeName() != null){
            examination.setTypeName(examinationUpdateRequest.getTypeName());
        }
        examinationRepository.save(examination);
        return examinationMapper.toDto(examination);
    }
}
