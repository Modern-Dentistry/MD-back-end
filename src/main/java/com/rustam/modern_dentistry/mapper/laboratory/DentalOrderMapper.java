package com.rustam.modern_dentistry.mapper.laboratory;

import com.rustam.modern_dentistry.dao.entity.laboratory.DentalOrder;
import com.rustam.modern_dentistry.dto.request.DentalOrderCreateReq;
import com.rustam.modern_dentistry.dto.response.read.DentalOrderTeethListResponse;
import com.rustam.modern_dentistry.dto.response.read.DentalOrderToothDetailResponse;
import com.rustam.modern_dentistry.dto.response.read.TechnicianOrderResponse;
import com.rustam.modern_dentistry.util.constants.Directory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.stream.Collectors;

import static com.rustam.modern_dentistry.util.constants.Directory.pathDentalOrder;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DentalOrderMapper {

    @Mapping(target = "teethList", ignore = true)
    @Mapping(target = "imagePaths", ignore = true)
    DentalOrder toEntity(DentalOrderCreateReq request);

    default TechnicianOrderResponse toResponse(DentalOrder o) {
        return TechnicianOrderResponse.builder()
                .id(o.getId())
                .checkDate(o.getCheckDate())
                .deliveryDate(o.getDeliveryDate())
                .description(o.getDescription())
                .orderDentureInfo(o.getOrderDentureInfo() != null ? o.getOrderDentureInfo() : null)
                .orderType(o.getOrderType())
                .orderStatus(o.getOrderStatus())
                .toothDetails(o.getToothDetails().stream().map(
                        toothDetail -> {
                            return DentalOrderToothDetailResponse.builder()
                                    .colorId(toothDetail.getColor().getId())
                                    .colorName(toothDetail.getColor().getName())
                                    .metalId(toothDetail.getMetal().getId())
                                    .metalName(toothDetail.getMetal().getName())
                                    .ceramicId(toothDetail.getCeramic().getId())
                                    .ceramicName(toothDetail.getCeramic().getName())
                                    .build();
                        }
                ).toList())
                .teethList(o.getTeethList().stream().map(
                        tooth -> {
                            return DentalOrderTeethListResponse.builder()
                                    .id(tooth.getId())
                                    .toothNo(tooth.getToothNo())
                                    .toothType(tooth.getToothType().toString())
                                    .toothLocation(tooth.getToothLocation().toString())
                                    .build();
                        }
                ).toList())
                .doctor(o.getDoctor().getName() + " " + o.getDoctor().getSurname())
                .patient(o.getPatient().getName() + " " + o.getPatient().getSurname())
                .technician(o.getTechnician().getName() + " " + o.getTechnician().getSurname())
                .urls(o.getImagePaths().stream().map(
                                fileName -> Directory.getUrl(pathDentalOrder, fileName))
                        .collect(Collectors.toList()))
                .build();
    }
}
