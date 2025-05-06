package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.repository.TechnicianRepository;
import com.rustam.modern_dentistry.dto.request.create.TechnicianCreateRequest;
import com.rustam.modern_dentistry.dto.request.criteria.PageCriteria;
import com.rustam.modern_dentistry.dto.request.read.ReservationSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.TechnicianUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.PageResponse;
import com.rustam.modern_dentistry.dto.response.read.ReservationReadResponse;
import com.rustam.modern_dentistry.dto.response.read.TechnicianReadResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.mapper.settings.TechnicianMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TechnicianService {
    private final PasswordEncoder passwordEncoder;
    private final TechnicianMapper technicianMapper;
    private final TechnicianRepository technicianRepository;

    public void create(TechnicianCreateRequest request) {
        checkIfUserAlreadyExist(request.getUsername());
        var entity = technicianMapper.toEntity(request);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        technicianRepository.save(entity);
    }

    public PageResponse<TechnicianReadResponse> read(PageCriteria pageCriteria) {
        return null;
    }

    public TechnicianReadResponse readById(Long id) {
        return null;
    }

    public void update(Long id, @Valid TechnicianUpdateRequest request) {
    }

    public void updateStatus(Long id) {
    }

    public void delete(Long id) {
    }

    public PageResponse<ReservationReadResponse> search(ReservationSearchRequest request, PageCriteria pageCriteria) {
        return null;
    }

    public InputStreamResource exportReservationsToExcel() {
        return null;
    }

    private void checkIfUserAlreadyExist(String username) {
        var existsByUsername = technicianRepository.existsByUsername(username);
        if (existsByUsername)
            throw new ExistsException("Bu adda istifadəçi adı mövciuddur" + username);
    }
}
