package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dto.request.read.PatientPlansReadCategoryAndOperationsRequest;
import com.rustam.modern_dentistry.dto.response.read.CategoryOfOperationDto;
import com.rustam.modern_dentistry.service.settings.operations.OperationTypeService;
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

    public List<CategoryOfOperationDto> readCategoryAndOperations(Long insuranceId) {
        return operationTypeService.getOperationsByInsurance(insuranceId);
    }

}
