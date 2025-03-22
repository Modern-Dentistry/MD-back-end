package com.rustam.modern_dentistry.mapper;

import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItemInsurance;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItemPrice;
import com.rustam.modern_dentistry.dto.request.create.OpTypeInsuranceRequest;
import com.rustam.modern_dentistry.dto.request.create.OpTypeItemCreateRequest;
import com.rustam.modern_dentistry.dto.request.create.OpTypeItemInsurances;
import com.rustam.modern_dentistry.dto.request.create.Prices;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OperationTypeItemMapper {
    OperationTypeItemMapper OP_TYPE_ITEM_MAPPER = Mappers.getMapper(OperationTypeItemMapper.class);

    @Mapping(target = "status", defaultValue = "ACTIVE")
    @Mapping(target = "prices", ignore = true)
    @Mapping(target = "insurances", ignore = true)
    OpTypeItem toEntity(OpTypeItemCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "opTypeItem", ignore = true)
    @Mapping(source = "insuranceCompanyId", target = "insuranceCompany.id")
    OpTypeItemInsurance toInsuranceEntity(OpTypeItemInsurances request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "opTypeItem", ignore = true)
    @Mapping(source = "priceTypeId", target = "priceType.id")
    OpTypeItemPrice toPriceCategoryEntity(Prices request);
}
