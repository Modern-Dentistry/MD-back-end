package com.rustam.modern_dentistry.service.settings;

import com.rustam.modern_dentistry.dao.repository.settings.BlacklistResultRepository;
import com.rustam.modern_dentistry.dto.request.UpdateBlacklistResultReq;
import com.rustam.modern_dentistry.dto.request.create.BlacklistResultCreateReq;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.read.BlacklistResultSearchReq;
import com.rustam.modern_dentistry.dto.response.read.BlacklistResultReadRes;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.mapper.settings.BlackListResultMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlacklistResultService {
    private final BlackListResultMapper blackListResultMapper;
    private final BlacklistResultRepository blacklistResultRepository;

    public void create(@Valid BlacklistResultCreateReq request) {
    }

    public PageResponse<BlacklistResultReadRes> read(PageCriteria pageCriteria) {
        return null;
    }

    public List<BlacklistResultReadRes> readList() {
        return null;
    }

    public BlacklistResultReadRes readById(Long id) {
        return null;
    }

    public void update(Long id, @Valid UpdateBlacklistResultReq request) {

    }

    public void updateStatus(Long id) {
    }

    public void delete(Long id) {
    }

    public PageResponse<BlacklistResultReadRes> search(BlacklistResultSearchReq request, PageCriteria pageCriteria) {
        return null;
    }

    public InputStreamResource exportReservationsToExcel() {
        return null;
    }
}
