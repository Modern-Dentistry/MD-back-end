package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.repository.OperationTypeItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperationTypeItemService {
    private final OperationTypeItemRepository repository;
//
//    public void create(OperationTypeItemCreateRequest request) {
//        var operationType = OPERATION_TYPE_ITEM_MAPPER.toEntity(request);
//        repository.save(operationType);
//    }
//
//    public PageResponse<OperationTypeItem> read(PageCriteria pageCriteria) {
//        Page<OperationTypeItem> response = repository.findAll(
//                PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount()));
//        return new PageResponse<>(response.getTotalPages(), response.getTotalElements(), response.getContent());
//    }
//
//    public PageResponse<OperationTypeItem> search(OperationTypeItemSearchRequest request, PageCriteria pageCriteria) {
//        Page<OperationTypeItem> response = repository.findAll(
//                OperationTypeItemSpecification.filterBy(request),
//                PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount()));
//        return new PageResponse<>(response.getTotalPages(), response.getTotalElements(), response.getContent());
//    }
//
//    public OperationTypeReadResponse readById(Long id) {
//        var operationType = getOperationTypeItemById(id);
//        return OPERATION_TYPE_ITEM_MAPPER.toReadDto(operationType);
//    }
//
//    public void update(Long id, OperationTypeUpdateRequest request) {
//        var operationType = getOperationTypeItemById(id);
//        OPERATION_TYPE_ITEM_MAPPER.updateOperationType(operationType, request);
//        repository.save(operationType);
//    }
//
//    public void updateStatus(Long id) {
//        var operationType = getOperationTypeItemById(id);
//        var status = operationType.getStatus() == Status.ACTIVE ? Status.PASSIVE : Status.ACTIVE;
//        operationType.setStatus(status);
//        repository.save(operationType);
//    }
//
//    public void delete(Long id) {
//        var operationType = getOperationTypeById(id);
//        repository.delete(operationType);
//    }
//
//    public InputStreamResource exportReservationsToExcel() {
//        List<OperationTypeItem> reservations = repository.findAll();
//        var list = reservations.stream().map(OPERATION_TYPE_ITEM_MAPPER::toExcelDto).toList();
//        try {
//            ByteArrayInputStream excelFile = ExcelUtil.dataToExcel(list, OperationTypeItemExcelResponse.class);
//            return new InputStreamResource(excelFile);
//        } catch (IOException e) {
//            throw new RuntimeException("Error generating Excel file", e);
//        }
//    }
//
//    private OperationTypeItem getOperationTypeItemById(Long id) {
//        return repository.findById(id).orElseThrow(
//                () -> new NotFoundException("OperationType not found with ID: " + id)
//        );
//    }
}
