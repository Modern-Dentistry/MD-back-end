package com.rustam.modern_dentistry.service.settings;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.settings.permission.ModulePermissionEntity;
import com.rustam.modern_dentistry.dao.entity.settings.permission.Permission;
import com.rustam.modern_dentistry.dao.repository.settings.PermissionRepository;
import com.rustam.modern_dentistry.dto.request.create.PermissionCreateRequest;
import com.rustam.modern_dentistry.dto.response.read.PermissionResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;

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
}
