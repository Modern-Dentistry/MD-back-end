package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpType;
import com.rustam.modern_dentistry.dao.repository.OperationTypeRepository;
import com.rustam.modern_dentistry.dto.request.create.OpTypeCreateRequest;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.read.OperationTypeSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.OpTypeUpdateRequest;
import com.rustam.modern_dentistry.dto.response.excel.OperationTypeExcelResponse;
import com.rustam.modern_dentistry.dto.response.read.InsDeducReadResponse;
import com.rustam.modern_dentistry.dto.response.read.OperationTypeReadResponse;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.util.ExcelUtil;
import com.rustam.modern_dentistry.util.specification.OperationTypeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static com.rustam.modern_dentistry.mapper.OperationTypeMapper.OP_TYPE_MAPPER;

@Service
@RequiredArgsConstructor
public class OperationTypeService {
    private final OperationTypeRepository repository;
    private final InsuranceCompanyService insuranceCompanyService;

    @Transactional
    public void create(OpTypeCreateRequest request) {
        var opType = OP_TYPE_MAPPER.toEntity(request);

        if (request.getInsurances() != null) {
            var insurances = request.getInsurances().stream()
                    .map(insuranceRequest -> {
                        var insuranceCompany = insuranceCompanyService.getInsuranceById(insuranceRequest.getInsuranceCompanyId());
                        var insurance = OP_TYPE_MAPPER.toInsuranceEntity(insuranceRequest);
                        insurance.setOpType(opType);
                        insurance.setInsuranceCompany(insuranceCompany);
                        return insurance;
                    }).toList();
            opType.setInsurances(insurances);
        }

        repository.save(opType);
    }

    public PageResponse<OperationTypeReadResponse> read(PageCriteria pageCriteria) {
        var response = repository.findAll(PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount()));
        var content = getContent(response.getContent());
        return new PageResponse<>(response.getTotalPages(), response.getTotalElements(), content);
    }

    public PageResponse<OperationTypeReadResponse> search(OperationTypeSearchRequest request, PageCriteria pageCriteria) {
        var response = repository.findAll(
                OperationTypeSpecification.filterBy(request),
                PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount()));
        var content = getContent(response.getContent());
        return new PageResponse<>(response.getTotalPages(), response.getTotalElements(), content);
    }

    public OperationTypeReadResponse readById(Long id) {
        var operationType = getOperationTypeById(id);
        var byOpTypeId = repository.findByOpTypeId(id);
        var readDto = OP_TYPE_MAPPER.toReadDto(operationType);
        readDto.setInsurances(byOpTypeId);
        return readDto;
    }

    @Transactional
    public void update(Long id, OpTypeUpdateRequest request) {
        var opType = getOperationTypeById(id);
        OP_TYPE_MAPPER.updateOpType(opType, request);

        if (request.getInsurances() != null) {
            var updatedInsurances = OP_TYPE_MAPPER.updateOpTypeInsurance(request.getInsurances(), opType);
            opType.setInsurances(updatedInsurances);
        }

        repository.save(opType);
    }

    public void updateStatus(Long id) {
        var operationType = getOperationTypeById(id);
        var status = operationType.getStatus() == Status.ACTIVE ? Status.PASSIVE : Status.ACTIVE;
        operationType.setStatus(status);
        repository.save(operationType);
    }

    public void delete(Long id) {
        var operationType = getOperationTypeById(id);
        repository.delete(operationType);
    }

    public InputStreamResource exportReservationsToExcel() {
        List<OpType> reservations = repository.findAll();
        var list = reservations.stream().map(OP_TYPE_MAPPER::toExcelDto).toList();
        try {
            ByteArrayInputStream excelFile = ExcelUtil.dataToExcel(list, OperationTypeExcelResponse.class);
            return new InputStreamResource(excelFile);
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel file", e);
        }
    }

    protected OpType getOperationTypeById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("OperationType not found with ID: " + id)
        );
    }

    private List<OperationTypeReadResponse> getContent(List<OpType> operationTypes) {
        return operationTypes
                .stream()
                .map(OP_TYPE_MAPPER::toReadDto)
                .toList();
    }

    public OpType findByCategoryName(String operationCategoryName) {
        return repository.findByCategoryName(operationCategoryName)
                .orElseThrow(() -> new NotFoundException("OperationType not found with name: " + operationCategoryName));
    }
}
