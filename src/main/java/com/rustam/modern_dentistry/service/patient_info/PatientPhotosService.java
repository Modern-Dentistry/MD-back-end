package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.repository.patient_info.PatientPhotosRepository;
import com.rustam.modern_dentistry.dto.request.create.PatPhotosCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatPhotosUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatPhotosReadRes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientPhotosService {
    private final PatientPhotosRepository patientPhotosRepository;

    public void create(@Valid PatPhotosCreateReq req, MultipartFile file) {
    }

    public List<PatPhotosReadRes> read(Long patientId) {
        return null;
    }

    public PatPhotosReadRes readById(Long id) {
        return null;
    }

    public void update(Long id, PatPhotosUpdateReq request, MultipartFile file) {
    }

    public void delete(Long id) {
    }
}
