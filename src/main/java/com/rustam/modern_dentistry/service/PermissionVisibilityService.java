package com.rustam.modern_dentistry.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionVisibilityService {

    private final Map<String, Set<String>> VISIBILITY_RULES = Map.of(
            "DOCTOR", Set.of("DOCTOR"),
            "HEAD_DOCTOR", Set.of("DOCTOR", "RECEPTION"),
            "RECEPTION", Set.of("RECEPTION")
    );

    public Set<String> getVisiblePermissions(String permission) {
        return VISIBILITY_RULES.getOrDefault(permission, Set.of(permission));
    }

    public boolean isAdminPermission(String permission) {
        return "SUPER_ADMIN".equals(permission) || "ADMIN".equals(permission);
    }

}
