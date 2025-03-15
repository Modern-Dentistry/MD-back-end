package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dao.repository.PatientRepository;
import com.rustam.modern_dentistry.dto.request.create.PatientCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.PatientSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.PatientUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.PatientCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.PatientReadResponse;
import com.rustam.modern_dentistry.dto.response.update.PatientUpdateResponse;
import com.rustam.modern_dentistry.mapper.PatientMapper;
import com.rustam.modern_dentistry.util.ExcelUtil;
import com.rustam.modern_dentistry.util.UtilService;
import com.rustam.modern_dentistry.util.specification.UserSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PatientService {

    PatientRepository patientRepository;
    UtilService utilService;
    PatientMapper patientMapper;
    ModelMapper modelMapper;
    DoctorService doctorService;

    public PatientCreateResponse create(PatientCreateRequest patientCreateRequest) {
        Doctor doctor = doctorService.findById(patientCreateRequest.getDoctor_id());
        Patient patient = Patient.builder()
                .name(patientCreateRequest.getName())
                .surname(patientCreateRequest.getSurname())
                .patronymic(patientCreateRequest.getPatronymic())
                .finCode(patientCreateRequest.getFinCode())
                .dateOfBirth(patientCreateRequest.getDateOfBirth())
                .phone(patientCreateRequest.getPhone())
                .email(patientCreateRequest.getEmail())
                .enabled(true)
                .doctor(doctor)
                .homePhone(patientCreateRequest.getHomePhone())
                .workPhone(patientCreateRequest.getWorkPhone())
                .workAddress(patientCreateRequest.getWorkAddress())
                .homeAddress(patientCreateRequest.getHomeAddress())
                .genderStatus(patientCreateRequest.getGenderStatus())
                .priceCategoryStatus(patientCreateRequest.getPriceCategoryStatus())
                .specializationStatus(patientCreateRequest.getSpecializationStatus())
                .registration_date(LocalDate.now())
                .role(Role.PATIENT.getValue())
                .build();
        patientRepository.save(patient);
        PatientCreateResponse patientCreateResponse = new PatientCreateResponse();
        modelMapper.map(patient, patientCreateResponse);
        return patientCreateResponse;
    }

    public PatientUpdateResponse update(PatientUpdateRequest patientUpdateRequest) {
        Patient patient = utilService.findByPatientId(patientUpdateRequest.getPatientId());
        modelMapper.map(patient, patient);
        patientRepository.save(patient);
        return patientMapper.toUpdatePatient(patient);
    }

    public List<PatientReadResponse> read() {
        List<Patient> users = patientRepository.findAll();
        return patientMapper.toDtos(users);
    }

    public PatientReadResponse readById(Long id) {
        Patient patient = utilService.findByPatientId(id);
        return patientMapper.toRead(patient);
    }

    public String delete(Long id) {
        Patient patient = utilService.findByPatientId(id);
        patientRepository.delete(patient);
        return "Qeydiyyatdan silindi";
    }

    public List<PatientReadResponse> search(PatientSearchRequest patientSearchRequest) {
        List<Patient> byNameAndSurnameAndFinCodeAndGenderStatusAndPhone =
                patientRepository.findAll(UserSpecification.filterBy(patientSearchRequest));
        return patientMapper.toDtos(byNameAndSurnameAndFinCodeAndGenderStatusAndPhone);
    }

    public InputStreamResource exportReservationsToExcel() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientReadResponse> list = patientMapper.toDtos(patients);
        try {
            ByteArrayInputStream excelFile = ExcelUtil.dataToExcel(list, PatientReadResponse.class);
            return new InputStreamResource(excelFile);
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel file", e);
        }
    }
}
