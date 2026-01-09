package com.rustam.modern_dentistry.service.patient_info.patientplan;

import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlan;
import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanMain;
import com.rustam.modern_dentistry.dao.entity.patient_info.patientplan.PatientPlanPartOfToothDetail;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import com.rustam.modern_dentistry.dao.repository.patient_info.patientplan.PatientPlansRepository;
import com.rustam.modern_dentistry.dto.request.create.PatientPlansCreateRequest;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PatientPlansCreateService {

    PatientPlansRepository patientPlansRepository;
    PatientPlansUpdateMainService patientPlansUpdateMainService;
    OperationTypeService operationTypeService;
    UtilService utilService;
    OperationTypeItemService operationTypeItemService;
    PatientPlanUtilService patientPlanUtilService;

    @Transactional
    public PatientPlansResponse create(PatientPlansCreateRequest req) {
        patientPlanUtilService.validate(
                req.getPatientPlanMainId(), req.getToothId(), req.getCategoryId(), req.getPartOfTeethIds(), req.getOperationId()
        );

        PatientPlan existingPatientPlan = patientPlanUtilService.existsByPlanMainIdAndToothIdAndCategoryIdAndOperationId(
                req.getPatientPlanMainId(), req.getToothId(), req.getCategoryId(), req.getOperationId()
        );

        if (existingPatientPlan != null) {
            Set<Long> existingPartOfToothIds = existingPatientPlan.getDetails().stream()
                    .map(PatientPlanPartOfToothDetail::getPartOfToothId)
                    .collect(Collectors.toSet());

            OpTypeItem operationTypeItem = operationTypeItemService.findById(req.getOperationId());

            for (Long partOfToothId : req.getPartOfTeethIds()) {
                if (!existingPartOfToothIds.contains(partOfToothId)) {
                    PatientPlanPartOfToothDetail detail = PatientPlanPartOfToothDetail.builder()
                            .patientPlan(existingPatientPlan)
                            .partOfToothId(partOfToothId)
                            .opTypeItem(operationTypeItem)
                            .build();

                    existingPatientPlan.getDetails().add(detail);
                }
            }

            PatientPlan updatedPlan = patientPlansRepository.save(existingPatientPlan);

            return patientPlanUtilService.mapper(updatedPlan);

        }

            PatientPlanMain mainPlan = patientPlansUpdateMainService
                    .findByIdAndStatusAndActionStatus(req.getPatientPlanMainId());

            OpTypeItem operations = operationTypeItemService.findById(req.getOperationId());
            if (!operations.getOpType().getId().equals(req.getCategoryId())) {
                throw new NotFoundException("Bütün əməliyyatlar seçilmiş kateqoriyaya aid olmalıdır");
            }

            PatientPlan patientPlan = PatientPlan.builder()
                    .patientPlanMain(mainPlan)
                    .toothId(req.getToothId())
                    .opType(operationTypeService.findById(req.getCategoryId()))
                    .status("C")
                    .actionStatus("C")
                    .createdBy(utilService.getCurrentUserId())
                    .createdDate(DateTimeUtil.toEpochMilli(LocalDateTime.now()))
                    .build();

            List<PatientPlanPartOfToothDetail> details = new ArrayList<>();

            for (Long partOfToothId : req.getPartOfTeethIds()) {
                PatientPlanPartOfToothDetail detail = PatientPlanPartOfToothDetail.builder()
                        .patientPlan(patientPlan)
                        .partOfToothId(partOfToothId)
                        .opTypeItem(operations)
                        .build();

                details.add(detail);
            }

            patientPlan.setDetails(details);

            PatientPlan savedPlan = patientPlansRepository.save(patientPlan);

            return patientPlanUtilService.mapper(savedPlan);
        }

    }
