package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dto.response.read.PatientInsuranceReadResponse;
import com.rustam.modern_dentistry.dto.response.read.ReadPatientsInsuranceResponse;
import com.rustam.modern_dentistry.mapper.patient_info.patientplan.PatientPlansMainMapper;
import com.rustam.modern_dentistry.service.patient_info.insurance.PatientInsuranceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientPlansReadPatientsInsuranceMainService {

    PatientInsuranceService patientInsuranceService;
    PatientPlansMainMapper patientPlansMainMapper;

    public List<String> readPatientsInsurance(Long patientInsuranceId) {
        List<String> companyName = patientInsuranceService.readAllById(patientInsuranceId).stream().map(PatientInsuranceReadResponse::getInsuranceCompanyName).toList();
        return patientPlansMainMapper.toStrings(companyName);
    }
}
