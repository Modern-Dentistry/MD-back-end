package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.Teeth;
import com.rustam.modern_dentistry.dao.repository.TeethRepository;
import com.rustam.modern_dentistry.dto.request.create.CreateTeethRequest;
import com.rustam.modern_dentistry.dto.request.read.TeethRequest;
import com.rustam.modern_dentistry.dto.request.update.UpdateTeethRequest;
import com.rustam.modern_dentistry.dto.response.read.TeethResponse;
import com.rustam.modern_dentistry.dto.response.update.TeethUpdateResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NoTeethFoundException;
import com.rustam.modern_dentistry.mapper.TeethMapper;
import com.rustam.modern_dentistry.util.specification.TeethSpecification;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TeethService {

    TeethRepository teethRepository;
    TeethMapper teethMapper;

    public void create(CreateTeethRequest createTeethRequest) {
        boolean existsTeethByToothNo = existsTeethByToothNo(createTeethRequest.getToothNo());
        if (existsTeethByToothNo){
            throw new ExistsException(("This tooth number is already available."));
        }
        Teeth teeth = Teeth.builder()
                .toothNo(createTeethRequest.getToothNo())
                .toothType(createTeethRequest.getToothType())
                .toothLocation(createTeethRequest.getToothLocation())
                .build();
        teethRepository.save(teeth);
    }

    public List<TeethResponse> read() {
        List<Teeth> teeth = teethRepository.findAllWithExaminations();
        return teeth.stream()
                .map(teethMapper::toTeethResponse)
                .collect(Collectors.toList());
    }

    public Teeth findById(Long id){
        return teethRepository.findById(id)
                .orElseThrow(() -> new NoTeethFoundException("no teeth found"));
    }

    public boolean existsTeethByToothNo(Long toothNo){
        return teethRepository.existsTeethByToothNo(toothNo);
    }

    public TeethUpdateResponse update(UpdateTeethRequest updateTeethRequest) {
        Teeth teeth = findById(updateTeethRequest.getId());
        boolean existsTeethByToothNo = existsTeethByToothNo(updateTeethRequest.getToothNo());
        if (existsTeethByToothNo){
            throw new ExistsException(("This tooth number is already available."));
        }
        if (updateTeethRequest.getToothNo() != null){
            teeth.setToothNo(updateTeethRequest.getToothNo());
        }
        if (updateTeethRequest.getToothLocation() != null){
            teeth.setToothLocation(updateTeethRequest.getToothLocation());
        }
        if (updateTeethRequest.getToothType() != null){
            teeth.setToothType(updateTeethRequest.getToothType());
        }
        teethRepository.save(teeth);
        return teethMapper.toUpdateResponse(teeth);
    }

    public List<TeethResponse> search(TeethRequest teethRequest) {
        List<Teeth> teeth = teethRepository.findAll(TeethSpecification.filterBy(teethRequest.getToothNo(), teethRequest.getToothType(), teethRequest.getToothLocation()));
        return teeth.stream()
                .map(teethMapper::toTeethResponse)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        Teeth teeth = findById(id);
        teethRepository.delete(teeth);
    }
}
