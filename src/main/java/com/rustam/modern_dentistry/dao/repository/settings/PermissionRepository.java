package com.rustam.modern_dentistry.dao.repository.settings;

import com.rustam.modern_dentistry.dao.entity.settings.permission.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission,Long> {
    Optional<Permission> findByPermissionName(String permissionName);
}
