package com.rustam.modern_dentistry.service.settings.implant;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.settings.implant.Implant;
import com.rustam.modern_dentistry.dao.entity.settings.implant.ImplantSizes;
import com.rustam.modern_dentistry.dao.repository.settings.implant.ImplantSizeRepository;
import com.rustam.modern_dentistry.dto.request.create.ImplantSizeCreateRequest;
import com.rustam.modern_dentistry.dto.response.read.ImplantSizeResponse;
import com.rustam.modern_dentistry.mapper.settings.implant.ImplantSizeMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ImplantSizeService {

    ImplantSizeRepository implantSizeRepository;
    ImplantService implantService;
    ImplantSizeMapper implantSizeMapper;

    public ImplantSizeResponse create(ImplantSizeCreateRequest implantSizeCreateRequest) {
        ImplantSizes implantSizes = ImplantSizes.builder()
                .implant(implantService.findById(implantSizeCreateRequest.getImplantSizeId()))
                .length(implantSizeCreateRequest.getLength())
                .diameter(implantSizeCreateRequest.getDiameter())
                .status(Status.ACTIVE)
                .build();
        return implantSizeMapper.toDto(implantSizes);
    }

    public List<ImplantSizeResponse> read() {
        return implantSizeMapper.toDtos(implantSizeRepository.findAll());
    }
}
