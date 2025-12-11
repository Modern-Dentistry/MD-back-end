package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlanMainRepository;
import com.rustam.modern_dentistry.dto.request.create.PatientPlansMainCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.PatientPlansMainUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.PatientPlansMainResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.mapper.patient_info.patientplan.PatientPlansMainMapper;
import com.rustam.modern_dentistry.service.settings.InsuranceCompanyService;
import com.rustam.modern_dentistry.util.DateTimeUtil;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatientPlansCreateMainService {

    PatientPlanMainRepository patientPlanMainRepository;
    UtilService utilService;
    PatientPlansMainMapper patientPlansMainMapper;
    InsuranceCompanyService insuranceCompanyService;

    public PatientPlansMainResponse create(PatientPlansMainCreateRequest req) {
        if (patientPlanMainRepository.existsByPlanName(req.getPlanName())){
            throw new ExistsException("Plan name already exists");
        }
        return patientPlansMainMapper.toDto(
                patientPlanMainRepository.save(
                        PatientPlanMain.builder()
                                .planName(req.getPlanName())
                                .key(req.getKey())
                                .insuranceCompany(
                                        insuranceCompanyService.getInsuranceById(req.getInsuranceId())
                                )
                                .patient(utilService.findByPatientId(req.getPatientId()))
                                .createdBy(utilService.getCurrentUserId())
                                .status("C")
                                .actionStatus("C")
                                .createdDate(DateTimeUtil.toEpochMilli(LocalDateTime.now()))
                                .build()
                )
        );
    }

}
