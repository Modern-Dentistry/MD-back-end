package com.rustam.modern_dentistry.service.settings.operations;

import com.rustam.modern_dentistry.dao.entity.settings.InsuranceCompany;
import com.rustam.modern_dentistry.dao.entity.settings.PriceCategory;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItemInsurance;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItemPrice;
import com.rustam.modern_dentistry.dao.repository.settings.InsuranceCompanyRepository;
import com.rustam.modern_dentistry.dao.repository.settings.PriceCategoryRepository;
import com.rustam.modern_dentistry.dao.repository.settings.operations.OperationTypeItemRepository;
import com.rustam.modern_dentistry.dto.request.create.OpTypeItemInsurances;
import com.rustam.modern_dentistry.dto.request.create.Prices;
import com.rustam.modern_dentistry.dto.request.update.OpTypeItemInsuranceUpdateRequest;
import com.rustam.modern_dentistry.dto.request.update.OpTypeItemPricesUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.OpTypeItemInsuranceDto;
import com.rustam.modern_dentistry.dto.response.read.OpTypeItemPricesDto;
import com.rustam.modern_dentistry.dto.response.read.OpTypeItemReadResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.rustam.modern_dentistry.mapper.settings.operations.OperationTypeItemMapper.OP_TYPE_ITEM_MAPPER;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class OperationTypeItemHelperService {
    private final OperationTypeItemRepository repository;
    private final InsuranceCompanyRepository insuranceCompanyRepository;
    private final PriceCategoryRepository priceCategoryRepository;

    protected OpTypeItem getOperationTypeItemById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("OperationType not found with ID: " + id)
        );
    }

    protected List<OpTypeItemInsurance> getOpTypeItemInsurances(List<OpTypeItemInsurances> request, OpTypeItem opTypeItem) {
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

    public OpTypeItemPrice getOpTypeItemPrice(Prices priceDto, OpTypeItem opTypeItem) {
        if (priceDto == null) {
            throw new NullPointerException("qiymet bosdu");
        }

        PriceCategory priceCategory = priceCategoryRepository.findById(priceDto.getPriceTypeId())
                .orElseThrow(() -> new NotFoundException("Price category not found"));

        return OpTypeItemPrice.builder()
                .price(priceDto.getPrice())
                .priceCategory(priceCategory)
                .opTypeItem(opTypeItem)
                .build();
    }

    private void validatePriceCategories(List<Long> requestedIds, Map<Long, PriceCategory> foundCategories) {
        if (requestedIds.size() != foundCategories.size()) {
            Set<Long> notFoundIds = new HashSet<>(requestedIds);
            notFoundIds.removeAll(foundCategories.keySet());
            throw new NotFoundException("Price categories not found for ids: " + notFoundIds);
        }
    }

    protected List<OpTypeItemReadResponse> getContent(List<OpTypeItem> operationTypes) {
        List<PriceCategory> allCategories = priceCategoryRepository.findAll();
        return operationTypes.stream()
                .map(opTypeItem -> {
                    var readDto = OP_TYPE_ITEM_MAPPER.toReadDto(opTypeItem);
                    // ✅ Artıq price tək obyektdir, List deyil
                    readDto.setPrice(opTypeItem.getAmount());
                    return readDto;
                })
                .collect(Collectors.toList());
    }

    protected OpTypeItemPrice updateOpTypePrice(
            OpTypeItemPricesUpdateRequest request,
            OpTypeItem opTypeItem) {

        if (request == null || request.getPrice() == null) {
            throw new NullPointerException("Price məlumatı tələb olunur");
        }

        if (opTypeItem.getPrice() != null) {
            return updateExistingPrice(
                    opTypeItem.getPrice(),
                    request.getPrice(),
                    request.getPriceCategoryId(),
                    opTypeItem
            );
        }
        else {
            return createNewPrice(
                    opTypeItem,
                    request.getPriceCategoryId(),
                    request.getPrice()
            );
        }
    }

    private OpTypeItemPrice updateExistingPrice(
            OpTypeItemPrice existingPrice,
            BigDecimal newPrice,
            Long priceCategoryId,
            OpTypeItem opTypeItem) {

        existingPrice.setPrice(newPrice);

        // Əgər price category dəyişirsə
        if (!existingPrice.getPriceCategory().getId().equals(priceCategoryId)) {
            PriceCategory priceCategory = priceCategoryRepository.findById(priceCategoryId)
                    .orElseThrow(() -> new NotFoundException("Price category not found"));
            existingPrice.setPriceCategory(priceCategory);
        }

        return existingPrice;
    }

    private OpTypeItemPrice createNewPrice(
            OpTypeItem opTypeItem,
            Long priceCategoryId,
            BigDecimal price) {

        PriceCategory priceCategory = priceCategoryRepository.findById(priceCategoryId)
                .orElseThrow(() -> new NotFoundException("Price category not found"));

        return OpTypeItemPrice.builder()
                .price(price)
                .priceCategory(priceCategory)
                .opTypeItem(opTypeItem)
                .build();
    }

    protected List<OpTypeItemInsurance> updateOpTypeInsurance(List<OpTypeItemInsuranceUpdateRequest> request, OpTypeItem opTypeItem) {
        Map<Long, OpTypeItemInsurance> currentInsurances = opTypeItem.getInsurances().stream()
                .collect(Collectors.toMap(p -> p.getInsuranceCompany().getId(), p -> p));

        return request.stream()
                .filter(p -> p.getAmount() != null) // Null olmayanları götür
                .map(insuranceDto ->
                        currentInsurances.containsKey(insuranceDto.getInsuranceCompanyId())
                                ? updateExistingInsurance(currentInsurances.get(insuranceDto.getInsuranceCompanyId()), insuranceDto.getAmount(), opTypeItem)
                                : createNewInsurance(opTypeItem, insuranceDto)
                )
                .collect(Collectors.toList());
    }

    private OpTypeItemInsurance updateExistingInsurance(OpTypeItemInsurance existingInsurance,
                                                        BigDecimal newAmount,
                                                        OpTypeItem opTypeItem) {
        existingInsurance.setAmount(newAmount);
        existingInsurance.setOpTypeItem(opTypeItem);
        return existingInsurance;
    }

    private OpTypeItemInsurance createNewInsurance(OpTypeItem opTypeItem,
                                                   OpTypeItemInsuranceUpdateRequest request) {
        InsuranceCompany company = InsuranceCompany.builder().id(request.getInsuranceCompanyId()).build();
        return new OpTypeItemInsurance(null, request.getName(), request.getAmount(),request.getSpecificCode(), opTypeItem, company);
    }
}