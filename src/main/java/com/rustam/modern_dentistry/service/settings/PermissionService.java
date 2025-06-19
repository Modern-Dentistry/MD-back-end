package com.rustam.modern_dentistry.service.settings;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.settings.permission.ModulePermissionEntity;
import com.rustam.modern_dentistry.dao.entity.settings.permission.Permission;
import com.rustam.modern_dentistry.dao.repository.settings.PermissionRepository;
import com.rustam.modern_dentistry.dto.ModulePermission;
import com.rustam.modern_dentistry.dto.request.create.PermissionCreateRequest;
import com.rustam.modern_dentistry.dto.response.read.InfoPermissionResponse;
import com.rustam.modern_dentistry.dto.response.read.PermissionResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.settings.permission.PermissionMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionCreateRequest permissionCreateRequest) {
        Permission permission = new Permission();
        permission.setPermissionName(permissionCreateRequest.getPermissionName());
        permission.setStatus(Status.ACTIVE);

        List<ModulePermissionEntity> modulePermissions = permissionCreateRequest.getPermissions().stream()
                .map(mp -> {
                    ModulePermissionEntity modulePermission = new ModulePermissionEntity();
                    modulePermission.setModuleUrl(mp.getModuleUrl());
                    modulePermission.setPermission(permission);
                    modulePermission.setActions(mp.getActions());
                    return modulePermission;
                }).toList();

        permission.setModulePermissions(modulePermissions);

        Permission saved = permissionRepository.save(permission);

        return PermissionResponse.builder()
                .id(saved.getId())
                .permissionName(saved.getPermissionName())
                .status(saved.getStatus())
                .build();
    }

    public Permission findByName(String permissionName) {
        return permissionRepository.findByPermissionName(permissionName)
                .orElseThrow(() -> new NotFoundException("Permission not found: " + permissionName));
    }

    public Permission findById(Long id){
        return permissionRepository.findWithModulePermissions(id)
                .orElseThrow(() -> new NotFoundException("Permission not found: " + id));
    }

    public List<PermissionResponse> read() {
        return permissionMapper.toDtos(permissionRepository.findAll());
    }

    @Transactional
    public InfoPermissionResponse info(Long id) {
        Permission permission = findById(id);
        return toDto(permission);
    }

    public InfoPermissionResponse toDto(Permission permission) {
        return InfoPermissionResponse.builder()
                .permissionName(permission.getPermissionName())
                .status(permission.getStatus())
                .modulePermissions(mapModulePermissions(permission.getModulePermissions()))
                .build();
    }

    private List<ModulePermission> mapModulePermissions(List<ModulePermissionEntity> entities) {
        return entities.stream()
                .map(e -> new ModulePermission(e.getModuleUrl(), e.getActions()))
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(LinkedHashSet::new), // sıranı qoruyur və təkrarı silir
                        ArrayList::new
                ));
    }
}
