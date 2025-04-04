package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientAnamnesis;
import com.rustam.modern_dentistry.dao.repository.patient_info.PatientAnamnesisRepository;
import com.rustam.modern_dentistry.dto.request.create.PatAnamnesisCreateReq;
import com.rustam.modern_dentistry.dto.response.read.PatAnamnesisReadRes;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.service.DoctorService;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.rustam.modern_dentistry.mapper.patient_info.PatientAnemnesisMapper.PATIENT_ANEMNESIS_MAPPER;

@Service
@RequiredArgsConstructor
public class PatientAnamnesisService {
    private final UtilService utilService;
    private final DoctorService doctorService;
    private final PatientAnamnesisRepository patientAnamnesisRepository;

    public void create(PatAnamnesisCreateReq req) {
        var entity = PATIENT_ANEMNESIS_MAPPER.toEntity(req);
        var currentUserId = utilService.getCurrentUserId();
        var doctor = doctorService.findById(UUID.fromString(currentUserId));
        var patient = utilService.findByPatientId(req.getPatientId());
        entity.setAddedByName(doctor.getName());
        entity.setPatient(patient);
        patientAnamnesisRepository.save(entity);
    }

    public List<PatAnamnesisReadRes> read() {
        return getContent(patientAnamnesisRepository.findAll());
    }

    public List<PatAnamnesisReadRes> readAllById(Long patientId) {
        var patientAnamnesisList = patientAnamnesisRepository.findByPatient_Id(patientId);
        return getContent(patientAnamnesisList);
    }

//    public void update(Long id, PatAnamnesisUpdateReq request) {
//        var patientAnamnesis = fetchPatientAnamnesisById(id);
//        PATIENT_ANEMNESIS_MAPPER.updatePatAnemnesis(patientAnamnesis, request);
//        patientAnamnesisRepository.save(patientAnamnesis);
//    }

    public void delete(Long id) {
        var patientAnamnesis = fetchPatientAnamnesisById(id);
        patientAnamnesisRepository.delete(patientAnamnesis);
    }

    private PatientAnamnesis fetchPatientAnamnesisById(Long id) {
        return patientAnamnesisRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də anamnez tapımadı:" + id)
        );
    }

    private List<PatAnamnesisReadRes> getContent(List<PatientAnamnesis> anamnesisList) {
        return anamnesisList
                .stream()
                .map(PATIENT_ANEMNESIS_MAPPER::toReadDto)
                .toList();
    }
}
