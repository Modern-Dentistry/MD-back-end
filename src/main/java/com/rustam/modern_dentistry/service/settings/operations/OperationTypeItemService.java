package com.rustam.modern_dentistry.service.settings.operations;

import com.rustam.modern_dentistry.dao.entity.settings.InsuranceCompany;
import com.rustam.modern_dentistry.dao.entity.settings.PriceCategory;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpType;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItemInsurance;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItemPrice;
import com.rustam.modern_dentistry.dao.repository.settings.InsuranceCompanyRepository;
import com.rustam.modern_dentistry.dao.repository.settings.operations.OperationTypeItemRepository;
import com.rustam.modern_dentistry.dao.repository.settings.PriceCategoryRepository;
import com.rustam.modern_dentistry.dto.request.create.OpTypeItemCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.OpTypeItemInsurances;
import com.rustam.modern_dentistry.dto.request.create.Prices;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.read.OpTypeItemSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.OpTypeItemUpdateRequest;
import com.rustam.modern_dentistry.dto.response.excel.OpTypeItemExcelResponse;
import com.rustam.modern_dentistry.dto.response.read.OpTypeItemReadByIdResponse;
import com.rustam.modern_dentistry.dto.response.read.OpTypeItemReadResponse;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.util.ExcelUtil;
import com.rustam.modern_dentistry.util.specification.settings.OpTypeItemSearchSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.ACTIVE;
import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.PASSIVE;
import static com.rustam.modern_dentistry.mapper.settings.operations.OperationTypeItemMapper.OP_TYPE_ITEM_MAPPER;
import static org.springframework.data.domain.PageRequest.of;

@Service
@RequiredArgsConstructor
public class OperationTypeItemService {
    private final OperationTypeItemRepository repository;
    private final InsuranceCompanyRepository insuranceCompanyRepository;
    private final PriceCategoryRepository priceCategoryRepository;

    public void create(OpTypeItemCreateRequest request) {
        var opTypeItem = OP_TYPE_ITEM_MAPPER.toEntity(request);
        var prices = getOpTypeItemPrices(request.getPrices(), opTypeItem);
        var insurances = getOpTypeItemInsurances(request.getInsurances(), opTypeItem);
        opTypeItem.setPrices(prices);
        opTypeItem.setInsurances(insurances);
        opTypeItem.setOpType(OpType.builder().id(request.getOpTypeId()).build());
        repository.save(opTypeItem);
    }

    public PageResponse<OpTypeItemReadResponse> read(Long id, PageCriteria pageCriteria) {
        var response = repository.findByOpTypeId(id, of(pageCriteria.getPage(), pageCriteria.getCount()));
        var list = getContent(response.getContent());
        return new PageResponse<>(response.getTotalPages(), response.getTotalElements(), list);
    }

    public PageResponse<OpTypeItemReadResponse> search(OpTypeItemSearchRequest request, PageCriteria pageCriteria) {
        var response = repository.findAll(
                OpTypeItemSearchSpec.filterBy(request),
                PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount()));
        var content = getContent(response.getContent());
        return new PageResponse<>(response.getTotalPages(), response.getTotalElements(), content);
    }

    public OpTypeItemReadByIdResponse readById(Long id) {
        var operationType = getOperationTypeItemById(id);
        var toReadDto = OP_TYPE_ITEM_MAPPER.toReadByIdDto(operationType);
        toReadDto.setInsurances(repository.findInsurancesByOpTypeItemId(id));
        toReadDto.setPrices(repository.findPricesByOpTypeItemId(id));
        return toReadDto;
    }

    public void update(Long id, OpTypeItemUpdateRequest request) {
        var opTypeItem = getOperationTypeItemById(id);
        OP_TYPE_ITEM_MAPPER.updateOpTypeItem(opTypeItem, request);

        if (request.getPrices() != null) {
            opTypeItem.setPrices(OP_TYPE_ITEM_MAPPER.updateOpTypePrices(request.getPrices(), opTypeItem));
        }

        if (request.getInsurances() != null) {
            opTypeItem.setInsurances(OP_TYPE_ITEM_MAPPER.updateOpTypeInsurance(request.getInsurances(), opTypeItem));
        }
        repository.save(opTypeItem);
    }

    public void updateStatus(Long id) {
        var operationType = getOperationTypeItemById(id);
        var status = operationType.getStatus() == ACTIVE ? PASSIVE : ACTIVE;
        operationType.setStatus(status);
        repository.save(operationType);
    }

    public void delete(Long id) {
        var operationType = getOperationTypeItemById(id);
        repository.delete(operationType);
    }

    public InputStreamResource exportReservationsToExcel() {
        List<OpTypeItem> operations = repository.findAll();
        var list = operations.stream().map(OP_TYPE_ITEM_MAPPER::toExcelDto).toList();
        try {
            ByteArrayInputStream excelFile = ExcelUtil.dataToExcel(list, OpTypeItemExcelResponse.class);
            return new InputStreamResource(excelFile);
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel file", e);
        }
    }

    // Helper methods
    private OpTypeItem getOperationTypeItemById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("OperationType not found with ID: " + id)
        );
    }

    private List<OpTypeItemInsurance> getOpTypeItemInsurances(List<OpTypeItemInsurances> request, OpTypeItem opTypeItem) {
        if (request != null) {
            List<Long> insuranceCompanyIds = request.stream().map(OpTypeItemInsurances::getInsuranceCompanyId).collect(Collectors.toList());
            Map<Long, InsuranceCompany> insuranceCompaniesMap = insuranceCompanyRepository.findByIdIn(insuranceCompanyIds)
                    .stream()
                    .collect(Collectors.toMap(InsuranceCompany::getId, insuranceCompany -> insuranceCompany));

            return request.stream()
                    .map(insuranceRequest -> {
                        var insuranceCompany = insuranceCompaniesMap.get(insuranceRequest.getInsuranceCompanyId());
                        var insurance = OP_TYPE_ITEM_MAPPER.toInsuranceEntity(insuranceRequest);
                        insurance.setOpTypeItem(opTypeItem);
                        insurance.setInsuranceCompany(insuranceCompany);
                        return insurance;
                    }).toList();
        }
        return null;
    }

    private List<OpTypeItemPrice> getOpTypeItemPrices(List<Prices> request, OpTypeItem opTypeItem) {
        if (request != null) {
            List<Long> priceCategoryIds = request.stream().map(Prices::getPriceTypeId).collect(Collectors.toList());
            Map<Long, PriceCategory> priceCategoryMap = priceCategoryRepository.findByIdIn(priceCategoryIds)
                    .stream()
                    .collect(Collectors.toMap(PriceCategory::getId, priceCategory -> priceCategory));

            return request.stream()
                    .map(insuranceRequest -> {
                        var priceCategory = priceCategoryMap.get(insuranceRequest.getPriceTypeId());
                        var opTypeItemPrice = OP_TYPE_ITEM_MAPPER.toPriceCategoryEntity(insuranceRequest);
                        opTypeItemPrice.setOpTypeItem(opTypeItem);
                        opTypeItemPrice.setPriceCategory(priceCategory);
                        return opTypeItemPrice;
                    }).toList();
        }
        return null;
    }

    private List<OpTypeItemReadResponse> getContent(List<OpTypeItem> operationTypes) {
        List<PriceCategory> allCategories = priceCategoryRepository.findAll();
        return operationTypes.stream()
                .map(opTypeItem -> {
                    var readDto = OP_TYPE_ITEM_MAPPER.toReadDto(opTypeItem);
                    readDto.setPrices(OP_TYPE_ITEM_MAPPER.mapPrices(opTypeItem.getPrices(), allCategories));
                    return readDto;
                })
                .collect(Collectors.toList());
    }
}
