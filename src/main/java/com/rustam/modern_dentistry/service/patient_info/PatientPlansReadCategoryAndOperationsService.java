package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dto.request.create.PatientPlansCreateRequest;
import com.rustam.modern_dentistry.dto.response.create.PatientPlansResponse;
import com.rustam.modern_dentistry.dto.response.read.CategoryOfOperationDto;
import com.rustam.modern_dentistry.dto.response.read.OpTypeItemReadResponse;
import com.rustam.modern_dentistry.dto.response.read.PatientPlansCategoryAndOperationsResponse;
import com.rustam.modern_dentistry.mapper.patient_info.patientplan.PatientPlansMapper;
import com.rustam.modern_dentistry.service.settings.operations.OperationTypeItemService;
import com.rustam.modern_dentistry.service.settings.operations.OperationTypeService;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientPlansReadCategoryAndOperationsService {

    OperationTypeService operationTypeService;
    OperationTypeItemService operationTypeItemService;

    public PatientPlansCategoryAndOperationsResponse readCategoryAndOperations() {
        List<CategoryOfOperationDto> categoryOfOperationDtos = operationTypeService.readCategoryOfOperations();
        List<OpTypeItemReadResponse> opTypeItemReadResponses = operationTypeItemService.readAll();
        return PatientPlansCategoryAndOperationsResponse.builder()
                .categoryOfOperationDtos(categoryOfOperationDtos)
                .opTypeItemReadResponses(opTypeItemReadResponses)
                .build();
    }

}
