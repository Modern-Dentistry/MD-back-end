package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.repository.patient_info.PatientXrayRepository;
import com.rustam.modern_dentistry.dto.request.create.PatXrayCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatXrayUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatXrayReadRes;
import com.rustam.modern_dentistry.mapper.patient_info.PatientXrayMapper;
import com.rustam.modern_dentistry.service.FileService;
import com.rustam.modern_dentistry.util.UtilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.rustam.modern_dentistry.util.constants.Directory.pathPatPhoto;

@Service
@RequiredArgsConstructor
public class PatientXrayService {
    private final UtilService utilService;
    private final FileService fileService;
    private final PatientXrayMapper patientXrayMapper;
    private final PatientXrayRepository patientXrayRepository;

    public void create(@Valid PatXrayCreateReq request, MultipartFile file) {
        var patient = utilService.findByPatientId(request.getPatientId());
        var newFileName = fileService.getNewFileName(file, "patient_xray_");
        var entity = patientXrayMapper.toEntity(request, newFileName);
        entity.setPatient(patient);
        fileService.checkFileIfExist(file);
        fileService.writeFile(file, pathPatPhoto, newFileName);
        patientXrayRepository.save(entity);
    }

    public List<PatXrayReadRes> read(Long patientId) {
        return null;
    }

    public PatXrayReadRes readById(Long id) {
        return null;
    }

    public void update(Long id, PatXrayUpdateReq request, MultipartFile file) {
    }

    public void delete(Long id) {
    }
}
