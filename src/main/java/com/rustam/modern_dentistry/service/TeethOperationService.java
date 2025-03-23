package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.settings.operations.OpType;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import com.rustam.modern_dentistry.dao.entity.teeth.Teeth;
import com.rustam.modern_dentistry.dao.entity.teeth.TeethOperation;
import com.rustam.modern_dentistry.dao.repository.TeethOperationRepository;
import com.rustam.modern_dentistry.dto.request.create.CreateTeethOperationRequest;
import com.rustam.modern_dentistry.dto.request.create.OpTypeAndItemRequest;
import com.rustam.modern_dentistry.dto.response.read.TeethOperationResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class TeethOperationService {

    TeethOperationRepository teethOperationRepository;
    TeethService teethService;
    OperationTypeService operationTypeService;

    @Transactional
    public void create(CreateTeethOperationRequest createTeethOperationRequest) {
        Teeth teeth = teethService.findById(createTeethOperationRequest.getTeethId());
        for (OpTypeAndItemRequest opTypeAndItemRequest : createTeethOperationRequest.getOpTypeAndItemRequests()) {
            OpType opType = operationTypeService.findByCategoryName(opTypeAndItemRequest.getOperationCategoryName());

            // Seçilmiş OpTypeItem-i tap
            OpTypeItem opTypeItem = opType.getOpTypeItems().stream()
                    .filter(item -> item.getOperationName().equals(opTypeAndItemRequest.getOperationName()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("OpTypeItem tapılmadı!"));

            // Yeni TeethOperation yarat
            TeethOperation teethOperation = TeethOperation.builder()
                    .teeth(teeth)
                    .opTypeItem(opTypeItem)
                    .build();

            teethOperationRepository.save(teethOperation);  // Bazaya yaz
        }
    }

    public List<TeethOperationResponse> read() {
        return teethOperationRepository.findAllTeethOperations();
    }
}
