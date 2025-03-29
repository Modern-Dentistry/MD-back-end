package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.entity.Examination;
import com.rustam.modern_dentistry.dao.entity.patient_info.PatientExaminations;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dao.repository.PatientExaminationsRepository;
import com.rustam.modern_dentistry.dto.request.create.PatientExaminationsCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.PatientExaminationsUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.PatientExaminationsCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.ExaminationResponse;
import com.rustam.modern_dentistry.dto.response.read.TeethResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.exception.custom.TeethExaminationNotFoundException;
import com.rustam.modern_dentistry.service.ExaminationService;
import com.rustam.modern_dentistry.service.PatientService;
import com.rustam.modern_dentistry.service.TeethService;
import com.rustam.modern_dentistry.util.UtilService;
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
public class PatientExaminationsService {

    PatientExaminationsRepository patientExaminationsRepository;
    ExaminationService examinationService;
    TeethService teethService;
    UtilService utilService;

    public List<ExaminationResponse> readExaminations() {
        return examinationService.read();
    }

    public List<TeethResponse> readTeeth() {
        return teethService.read();
    }

    public PatientExaminationsCreateResponse create(PatientExaminationsCreateRequest patientExaminationsCreateRequest) {
        Patient patient = utilService.findByPatientId(patientExaminationsCreateRequest.getPatientId());
        Examination examination = examinationService.findById(patientExaminationsCreateRequest.getExaminationId());
        boolean existsPatientExaminationsByPatientAndToothNumber = patientExaminationsRepository.existsPatientExaminationsByPatientAndToothNumber(patientExaminationsCreateRequest.getPatientId(), patientExaminationsCreateRequest.getToothNumber());
        if (existsPatientExaminationsByPatientAndToothNumber){
            throw new ExistsException("These examinations are available for this patient.");
        }
        List<Long> teethNo = new ArrayList<>();
        patientExaminationsCreateRequest.getToothNumber().forEach(toothNumber -> {
            PatientExaminations patientExaminations = PatientExaminations.builder()
                    .patient(patient)
                    .toothNumber(toothNumber)
                    .diagnosis(examination.getTypeName())
                    .build();
            patientExaminationsRepository.save(patientExaminations);
            teethNo.add(toothNumber);
        });
        return PatientExaminationsCreateResponse.builder()
                .patientId(patientExaminationsCreateRequest.getPatientId())
                .toothNo(teethNo)
                .diagnosis(examination.getTypeName())
                .build();
    }

    public PatientExaminations findById(Long id){
        return patientExaminationsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("This patient does not have these tests."));
    }

    public PatientExaminationsCreateResponse update(PatientExaminationsUpdateRequest patientExaminationsUpdateRequest) {
        PatientExaminations patientExaminations = findById(patientExaminationsUpdateRequest.getId());
        Patient patient = utilService.findByPatientId(patientExaminationsUpdateRequest.getPatientId());
        Examination examination = examinationService.findById(patientExaminationsUpdateRequest.getExaminationId());
        boolean existsPatientExaminationsByPatientAndToothNumber = patientExaminationsRepository.existsPatientExaminationsByPatientAndToothNumber(patientExaminationsUpdateRequest.getPatientId(), patientExaminationsUpdateRequest.getToothNumber());
        if (existsPatientExaminationsByPatientAndToothNumber){
            throw new ExistsException("These examinations are available for this patient.");
        }
        List<Long> teethNo = new ArrayList<>();
        patientExaminationsUpdateRequest.getToothNumber().forEach(toothNumber -> {
            patientExaminations.setPatient(patient);
            patientExaminations.setToothNumber(toothNumber);
            patientExaminations.setDiagnosis(examination.getTypeName());
            patientExaminationsRepository.save(patientExaminations);
            teethNo.add(toothNumber);
        });
        return PatientExaminationsCreateResponse.builder()
                .patientId(patientExaminationsUpdateRequest.getPatientId())
                .toothNo(teethNo)
                .diagnosis(examination.getTypeName())
                .build();
    }
}
