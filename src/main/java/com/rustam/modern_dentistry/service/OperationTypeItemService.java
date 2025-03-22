package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.settings.InsuranceCompany;
import com.rustam.modern_dentistry.dao.entity.settings.PriceCategory;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpType;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItemInsurance;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItemPrice;
import com.rustam.modern_dentistry.dao.repository.InsuranceCompanyRepository;
import com.rustam.modern_dentistry.dao.repository.OperationTypeItemRepository;
import com.rustam.modern_dentistry.dao.repository.PriceCategoryRepository;
import com.rustam.modern_dentistry.dto.request.create.OpTypeItemCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.OpTypeItemInsurances;
import com.rustam.modern_dentistry.dto.request.create.Prices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rustam.modern_dentistry.mapper.OperationTypeItemMapper.OP_TYPE_ITEM_MAPPER;

@Service
@RequiredArgsConstructor
public class OperationTypeItemService {
    private final OperationTypeItemRepository repository;
    private final InsuranceCompanyService insuranceCompanyService;
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

    private List<OpTypeItemInsurance> getOpTypeItemInsurances(List<OpTypeItemInsurances> request, OpTypeItem opTypeItem) {
        if (request != null) {
            // Collect all the insurance company IDs
            List<Long> insuranceCompanyIds = request.stream()
                    .map(OpTypeItemInsurances::getInsuranceCompanyId)
                    .collect(Collectors.toList());

            // Fetch all insurance companies in one go
            Map<Long, InsuranceCompany> insuranceCompaniesMap = insuranceCompanyRepository.findByIdIn(insuranceCompanyIds)
                    .stream()
                    .collect(Collectors.toMap(InsuranceCompany::getId, insuranceCompany -> insuranceCompany));

            // Process the insurances and map them with the fetched insurance companies
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
            // Collect all the price type IDs
            List<Long> priceCategoryIds = request.stream()
                    .map(Prices::getPriceTypeId)
                    .collect(Collectors.toList());

            // Fetch all insurance companies in one go
            Map<Long, PriceCategory> insuranceCompaniesMap = priceCategoryRepository.findByIdIn(priceCategoryIds)
                    .stream()
                    .collect(Collectors.toMap(PriceCategory::getId, priceCategory -> priceCategory));

            // Process the insurances and map them with the fetched insurance companies
            return request.stream()
                    .map(insuranceRequest -> {
                        var priceCategory = insuranceCompaniesMap.get(insuranceRequest.getPriceTypeId());
                        var opTypeItemPrice = OP_TYPE_ITEM_MAPPER.toPriceCategoryEntity(insuranceRequest);
                        opTypeItemPrice.setOpTypeItem(opTypeItem);
                        opTypeItemPrice.setPriceType(priceCategory);
                        return opTypeItemPrice;
                    }).toList();
        }
        return null;
    }
}
