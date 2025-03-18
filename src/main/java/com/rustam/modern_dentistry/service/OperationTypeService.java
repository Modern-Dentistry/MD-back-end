package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.OperationType;
import com.rustam.modern_dentistry.dao.entity.Reservation;
import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.repository.OperationTypeRepository;
import com.rustam.modern_dentistry.dto.request.create.OperationTypeCreateRequest;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.read.OperationTypeSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.OperationTypeUpdateRequest;
import com.rustam.modern_dentistry.dto.response.excel.OperationTypeExcelResponse;
import com.rustam.modern_dentistry.dto.response.read.OperationTypeReadResponse;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.dto.response.read.ReservationReadResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.util.ExcelUtil;
import com.rustam.modern_dentistry.util.specification.OperationTypeSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static com.rustam.modern_dentistry.mapper.OperationTypeMapper.OPERATION_TYPE_MAPPER;

@Service
@RequiredArgsConstructor
public class OperationTypeService {
    private final OperationTypeRepository repository;

    public void create(OperationTypeCreateRequest request) {
        var operationType = OPERATION_TYPE_MAPPER.toEntity(request);
        repository.save(operationType);
    }

    public PageResponse<OperationType> read(PageCriteria pageCriteria) {
        Page<OperationType> response = repository.findAll(
                PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount()));
        return new PageResponse<>(response.getTotalPages(), response.getTotalElements(), response.getContent());
    }

    public PageResponse<OperationType> search(OperationTypeSearchRequest request, PageCriteria pageCriteria) {
        Page<OperationType> response = repository.findAll(
                OperationTypeSpecification.filterBy(request),
                PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount()));
        return new PageResponse<>(response.getTotalPages(), response.getTotalElements(), response.getContent());
    }

    public OperationTypeReadResponse readById(Long id) {
        var operationType = getOperationTypeById(id);
        return OPERATION_TYPE_MAPPER.toReadDto(operationType);
    }

    public void update(Long id, OperationTypeUpdateRequest request) {
        var operationType = getOperationTypeById(id);
        OPERATION_TYPE_MAPPER.updateOperationType(operationType, request);
        repository.save(operationType);
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
        List<OperationType> reservations = repository.findAll();
        var list = reservations.stream().map(OPERATION_TYPE_MAPPER::toExcelDto).toList();
        try {
            ByteArrayInputStream excelFile = ExcelUtil.dataToExcel(list, OperationTypeExcelResponse.class);
            return new InputStreamResource(excelFile);
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel file", e);
        }
    }

    private OperationType getOperationTypeById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("OperationType not found with ID: " + id)
        );
    }
}
