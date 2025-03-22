package com.rustam.modern_dentistry.mapper;

import com.rustam.modern_dentistry.dao.entity.settings.InsuranceCompany;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpType;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeInsurance;
import com.rustam.modern_dentistry.dto.request.create.OpTypeCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.OpTypeInsuranceRequest;
import com.rustam.modern_dentistry.dto.request.update.OpTypeUpdateRequest;
import com.rustam.modern_dentistry.dto.response.excel.OperationTypeExcelResponse;
import com.rustam.modern_dentistry.dto.response.read.OperationTypeReadResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OperationTypeMapper {
    OperationTypeMapper OP_TYPE_MAPPER = Mappers.getMapper(OperationTypeMapper.class);

    @Mapping(target = "status", defaultValue = "ACTIVE")
    @Mapping(target = "insurances", ignore = true)
    OpType toEntity(OpTypeCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "opType", ignore = true) // OpType sonra əlavə olunacaq
    @Mapping(source = "insuranceCompanyId", target = "insuranceCompany.id")
    OpTypeInsurance toInsuranceEntity(OpTypeInsuranceRequest request);

    @Mapping(target = "insurances", ignore = true) // OpType sonra əlavə olunacaq
    @Mapping(target = "opTypeItemCount", expression = "java(countInsurances(entity))")
    OperationTypeReadResponse toReadDto(OpType entity);

    OperationTypeExcelResponse toExcelDto(OpType entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "insurances", ignore = true)
    @Mapping(target = "opTypeItems", ignore = true)
    void updateOpType(@MappingTarget OpType entity, OpTypeUpdateRequest request);


    default long countInsurances(OpType entity) {
        return entity.getInsurances().size();
    }

    default List<OpTypeInsurance> updateOpTypeInsurance(List<OpTypeInsuranceRequest> request, OpType opType) {
        List<OpTypeInsurance> updatedInsurances = new ArrayList<>(); // Create a new list for the updated insurances

        request.forEach(insuranceRequest -> {
            OpTypeInsurance insurance = opType.getInsurances().stream()
                    .filter(i -> i.getInsuranceCompany() != null && i.getInsuranceCompany().getId().equals(insuranceRequest.getInsuranceCompanyId()))
                    .findFirst()
                    .orElse(new OpTypeInsurance()); // Create new if not found

            insurance.setDeductiblePercentage(insuranceRequest.getDeductiblePercentage());
            insurance.setOpType(opType);
            insurance.setInsuranceCompany(InsuranceCompany.builder().id(insuranceRequest.getInsuranceCompanyId()).build()); // Set insurance company reference
            updatedInsurances.add(insurance);
        });

        return updatedInsurances;
    }
}
