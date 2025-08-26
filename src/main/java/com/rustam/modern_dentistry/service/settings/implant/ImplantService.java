package com.rustam.modern_dentistry.service.settings.implant;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.settings.implant.Implant;
import com.rustam.modern_dentistry.dao.repository.settings.implant.ImplantRepository;
import com.rustam.modern_dentistry.dto.request.create.ImplantCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.ImplantSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.ImplantStatusUpdateRequest;
import com.rustam.modern_dentistry.dto.request.update.ImplantUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.ImplantReadResponse;
import com.rustam.modern_dentistry.dto.response.read.ImplantResponse;
import com.rustam.modern_dentistry.dto.response.read.ImplantSizesRead;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.settings.implant.ImplantMapper;
import com.rustam.modern_dentistry.util.UtilService;
import com.rustam.modern_dentistry.util.specification.settings.implant.ImplantSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ImplantService {

    ImplantRepository implantRepository;
    ImplantMapper implantMapper;
    UtilService utilService;

    public ImplantResponse create(ImplantCreateRequest implantCreateRequest) {
        boolean existsImplantByImplantBrandName = existsImplantByImplantBrandName(implantCreateRequest.getImplantBrandName());
        if (existsImplantByImplantBrandName){
            throw new ExistsException("bu implant db-de movcuddur");
        }
        Implant implant = implantRepository.save(
                Implant.builder()
                        .implantBrandName(implantCreateRequest.getImplantBrandName())
                        .status(Status.ACTIVE)
                        .build()
        );
        return implantMapper.toDto(implant);
    }

    private boolean existsImplantByImplantBrandName(String implantBrandName) {
        return implantRepository.existsImplantByImplantBrandName(implantBrandName);
    }

    @Transactional(readOnly = true)
    public List<ImplantReadResponse> read() {
        List<Implant> implants = implantRepository.findAll();
        return implantToDtos(implants);
    }

    private List<ImplantReadResponse> implantToDtos(List<Implant> implants) {
        return implants.stream()
                   .map(this::toDto)
                   .toList();
}

    private ImplantReadResponse toDto(Implant implant) {
        return ImplantReadResponse.builder()
                .id(implant.getId())
                .implantBrandName(implant.getImplantBrandName())
                .status(implant.getStatus())
                .implantSizesReads(
                        implant.getImplantSizes() != null ?
                                implant.getImplantSizes().stream()
                                        .map(size -> new ImplantSizesRead(
                                                size.getId(),
                                                size.getLength(),
                                                size.getDiameter(),
                                                size.getStatus()))
                                        .toList() :
                                null
                )
                .build();
    }

    public List<ImplantReadResponse> search(ImplantSearchRequest implantSearchRequest) {
        return implantMapper.toDtos(
                implantRepository.findAll(ImplantSpecification.filterBy(implantSearchRequest))
        );
    }

    public ImplantResponse update(ImplantUpdateRequest implantUpdateRequest) {
        Implant implant = findById(implantUpdateRequest.getImplantId());
        utilService.updateFieldIfPresent(implantUpdateRequest.getImplantBrandName(),implant::setImplantBrandName);
        implantRepository.save(implant);
        return implantMapper.toDto(implant);
    }

    public Implant findById(Long implantId) {
        return implantRepository.findById(implantId)
                .orElseThrow(() -> new NotFoundException("bele bir implant tapilmadi"));
    }

    public Status statusUpdate(ImplantStatusUpdateRequest implantStatusUpdateRequest) {
        Implant implant = findById(implantStatusUpdateRequest.getImplantId());
        implant.setStatus(implantStatusUpdateRequest.getStatus());
        implantRepository.save(implant);
        return implant.getStatus();
    }

    public void delete(Long id) {
        Implant implant = findById(id);
        implantRepository.delete(implant);
    }
}