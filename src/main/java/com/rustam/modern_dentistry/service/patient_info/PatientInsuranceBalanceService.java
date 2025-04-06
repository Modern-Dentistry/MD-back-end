package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientInsuranceBalance;
import com.rustam.modern_dentistry.dao.repository.patient_info.PatientInsuranceBalanceRepository;
import com.rustam.modern_dentistry.dto.request.create.PatInsuranceBalanceCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatInsuranceBalanceUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatInsuranceBalanceReadResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.patient_info.PatientInsuranceBalanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.ACTIVE;
import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.PASSIVE;

@Service
@RequiredArgsConstructor
public class PatientInsuranceBalanceService {
    private final PatientInsuranceService patientInsuranceService;
    private final PatientInsuranceBalanceMapper patientInsuranceBalanceMapper;
    private final PatientInsuranceBalanceRepository patientInsuranceBalanceRepository;

    public void create(PatInsuranceBalanceCreateReq request) {
        checkDate(request.getDate());
        var patientInsurance = patientInsuranceService.getPatientInsuranceById(request.getPatientInsuranceId());
        var entity = patientInsuranceBalanceMapper.toEntity(request);
        entity.setPatientInsurance(patientInsurance);
        patientInsuranceBalanceRepository.save(entity);
    }

    public List<PatInsuranceBalanceReadResponse> read(Long patientInsuranceId) {
        var patientInsurances = patientInsuranceBalanceRepository.findAllByPatientInsurance_Id(patientInsuranceId);
        return patientInsurances.stream()
                .map(patientInsuranceBalanceMapper::toReadDto)
                .toList();
    }

    public PatInsuranceBalanceReadResponse readById(Long id) {
        var patientInsurance = getPatientInsurance(id);
        return patientInsuranceBalanceMapper.toReadDto(patientInsurance);
    }

    public void update(Long id, PatInsuranceBalanceUpdateReq request) {
        checkDate(request.getDate());
        var patientInsurance = getPatientInsurance(id);
        patientInsuranceBalanceMapper.update(patientInsurance, request);
        patientInsuranceBalanceRepository.save(patientInsurance);
    }

    public void updateStatus(Long id) {
        var patientInsurance = getPatientInsurance(id);
        patientInsurance.setStatus(patientInsurance.getStatus() == ACTIVE ? PASSIVE : ACTIVE);
        patientInsuranceBalanceRepository.save(patientInsurance);
    }

    public void delete(Long id) {
        var patientInsurance = getPatientInsurance(id);
        patientInsuranceBalanceRepository.delete(patientInsurance);
    }

    private PatientInsuranceBalance getPatientInsurance(Long id) {
        return patientInsuranceBalanceRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də qalıq məbləği tapımadı:" + id)
        );
    }

    private void checkDate(LocalDate date) {
        var result = patientInsuranceBalanceRepository.existsByDate(date);
        if (result) throw new ExistsException("Bu tarix artıq əlavə edilib.");
    }
}
