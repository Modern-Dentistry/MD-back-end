package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.InsuranceCompany;
import com.rustam.modern_dentistry.dao.repository.InsuranceCompanyRepository;
import com.rustam.modern_dentistry.dto.request.create.InsuranceCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.ReservationUpdateRequest;
import com.rustam.modern_dentistry.dto.request.update.UpdateICRequest;
import com.rustam.modern_dentistry.dto.response.read.InsuranceReadResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.ACTIVE;
import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.PASSIVE;
import static com.rustam.modern_dentistry.mapper.InsuranceCompanyMapper.INSURANCE_COMPANY_MAPPER;


@Service
@RequiredArgsConstructor
public class InsuranceCompanyService {
    private final InsuranceCompanyRepository repository;

    public void create(InsuranceCreateRequest request) {
        var insurance = INSURANCE_COMPANY_MAPPER.toEntity(request);
        repository.save(insurance);
    }

    public List<InsuranceReadResponse> read() {
        var insurances = repository.findAll();
        return insurances.stream().map(INSURANCE_COMPANY_MAPPER::toReadDto).toList();
    }

    public InsuranceReadResponse readById(Long id) {
        var insurance = getInsuranceById(id);
        return INSURANCE_COMPANY_MAPPER.toReadDto(insurance);
    }

    public void update(Long id, UpdateICRequest request) {
        var insuranceCompany = getInsuranceById(id);
        INSURANCE_COMPANY_MAPPER.updateInsuranceCompany(insuranceCompany, request);
        repository.save(insuranceCompany);
    }

    public void updateStatus(Long id) {
        var insurance = getInsuranceById(id);
        var status = insurance.getStatus() == ACTIVE ? PASSIVE : ACTIVE;
        insurance.setStatus(status);
        repository.save(insurance);
    }

    public void delete(Long id) {
        var insurance = getInsuranceById(id);
        repository.delete(insurance);
    }

    private InsuranceCompany getInsuranceById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də növbə tapımadı: " + id)
        );
    }
}
