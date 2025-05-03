package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.PatientBlacklist;
import com.rustam.modern_dentistry.dao.repository.PatientBlacklistRepository;
import com.rustam.modern_dentistry.dto.request.create.PatBlacklistCreateReq;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.read.PatBlacklistSearchReq;
import com.rustam.modern_dentistry.dto.request.update.PatBlacklistUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.dto.response.read.PatBlacklistReadRes;
import com.rustam.modern_dentistry.dto.response.read.ReservationReadResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.PatientBlacklistMapper;
import com.rustam.modern_dentistry.service.settings.BlacklistResultService;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientBlacklistService {
    private final UtilService utilService;
    private final BlacklistResultService blResService;
    private final PatientBlacklistMapper patientBlacklistMapper;
    private final PatientBlacklistRepository patientBlacklistRepository;

    public void create(PatBlacklistCreateReq request) {
        var patient = utilService.findByPatientId(request.getPatientId());
        var blacklistResult = blResService.getBlackListById(request.getBlacklistId());
        var entity = patientBlacklistMapper.toEntity(blacklistResult, patient);
        patientBlacklistRepository.save(entity);
    }

    public PageResponse<PatBlacklistReadRes> read(PageCriteria pageCriteria) {
        var patientBlacklists = patientBlacklistRepository.findAll(
                PageRequest.of(pageCriteria.getPage(), pageCriteria.getCount()));

        return new PageResponse<>(
                patientBlacklists.getTotalPages(),
                patientBlacklists.getTotalElements(),
                getContent(patientBlacklists.getContent()));
    }

    public PatBlacklistReadRes readById(Long id) {
        var entity = getPatientBlacklistById(id);
        return patientBlacklistMapper.toReadDto(entity);

    }

    public void update(Long id, PatBlacklistUpdateReq request) {
        var patientBlacklistById = getPatientBlacklistById(id);
        patientBlacklistMapper.update(patientBlacklistById, request);
    }

    public void delete(Long id) {
        var entity = getPatientBlacklistById(id);
        patientBlacklistRepository.delete(entity);
    }

    public PageResponse<ReservationReadResponse> search(PatBlacklistSearchReq request, PageCriteria pageCriteria) {
        return null;
    }

    public InputStreamResource exportReservationsToExcel() {
        return null;
    }

    private PatientBlacklist getPatientBlacklistById(Long id) {
        return patientBlacklistRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də pasiyent qara siyahı tapımadı: " + id)
        );
    }

    private List<PatBlacklistReadRes> getContent(List<PatientBlacklist> content) {
        return content.stream().map(patientBlacklistMapper::toReadDto).toList();
    }
}
