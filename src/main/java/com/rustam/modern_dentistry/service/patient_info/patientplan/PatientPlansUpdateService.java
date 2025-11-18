package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanPartOfToothDetail;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlansRepository;
import com.rustam.modern_dentistry.dto.request.update.PatientPlanUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.PatientPlansResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.service.settings.operations.OperationTypeItemService;
import com.rustam.modern_dentistry.service.settings.operations.OperationTypeService;
import com.rustam.modern_dentistry.util.DateTimeUtil;
import com.rustam.modern_dentistry.util.PatientPlanUtilService;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PatientPlansUpdateService {

    PatientPlanUtilService patientPlanUtilService;
    PatientPlansRepository patientPlansRepository;
    UtilService utilService;
    PatientPlansUpdateMainService patientPlansUpdateMainService;
    OperationTypeItemService operationTypeItemService;
    OperationTypeService operationTypeService;

    public PatientPlansResponse update(PatientPlanUpdateRequest req) {
        patientPlanUtilService.validate(
                req.getPatientPlanMainId(),req.getToothId(),req.getCategoryId(),req.getPartOfTeethIds(),req.getOperationId()
        );

        PatientPlanMain mainPlan = patientPlansUpdateMainService
                .findByIdAndStatusAndActionStatus(req.getPatientPlanMainId());

        OpTypeItem operations = operationTypeItemService.findById(req.getOperationId());
        if (!operations.getOpType().getId().equals(req.getCategoryId())) {
            throw new NotFoundException("Bütün əməliyyatlar seçilmiş kateqoriyaya aid olmalıdır");
        }

        PatientPlan patientPlan = patientPlanUtilService.findById(req.getId());
        List<PatientPlanPartOfToothDetail> details = mapDetails(req.getPartOfTeethIds(), patientPlan, operations);
        PatientPlan saved = patientPlansRepository.save(mapToEntity(patientPlan, mainPlan, req,details));
        return patientPlanUtilService.mapper(saved);
    }

    private PatientPlan mapToEntity(PatientPlan patientPlan, PatientPlanMain mainPlan, PatientPlanUpdateRequest req, List<PatientPlanPartOfToothDetail> details) {
        utilService.updateFieldIfPresent(req.getToothId(),patientPlan::setToothId);
        patientPlan.setPatientPlanMain(mainPlan);
        patientPlan.setDetails(details);
        patientPlan.setOpType(operationTypeService.findById(req.getCategoryId()));
        patientPlan.setStatus("A");
        patientPlan.setActionStatus("U");
        patientPlan.setUpdatedBy(utilService.getCurrentUserId());
        patientPlan.setUpdatedDate(DateTimeUtil.toEpochMilli(LocalDateTime.now()));
        return patientPlan;
    }

    private List<PatientPlanPartOfToothDetail> mapDetails(List<Long> partOfTeethIds, PatientPlan patientPlan, OpTypeItem operations) {
        List<PatientPlanPartOfToothDetail> details = new ArrayList<>();

        for (Long partOfToothId : partOfTeethIds) {
            PatientPlanPartOfToothDetail detail = PatientPlanPartOfToothDetail.builder()
                    .patientPlan(patientPlan)
                    .partOfToothId(partOfToothId)
                    .opTypeItem(operations)
                    .build();

            details.add(detail);
        }

        patientPlan.setDetails(details);
        return details;
    }
}
