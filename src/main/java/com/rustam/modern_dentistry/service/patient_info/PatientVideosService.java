package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientVideos;
import com.rustam.modern_dentistry.dao.repository.patient_info.PatientVideosRepository;
import com.rustam.modern_dentistry.dto.request.create.PatVideosCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatVideosUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatVideosReadRes;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientVideosService {
    private final PatientVideosRepository patientVideosRepository;

    public void create(@Valid PatVideosCreateReq request, MultipartFile file) {
    }

    public List<PatVideosReadRes> read(Long patientId) {
        return null;
    }

    public PatVideosReadRes readById(Long id) {
        return null;
    }

    public void update(Long id, PatVideosUpdateReq request, MultipartFile file) {
    }

    public void delete(Long id) {
    }

    private PatientVideos getPatientVideos(Long id) {
        return patientVideosRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də istifadəçi videosu tapımadı:" + id)
        );
    }
}
