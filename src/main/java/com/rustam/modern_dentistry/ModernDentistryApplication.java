package com.rustam.modern_dentistry;

import com.rustam.modern_dentistry.dao.entity.enums.PermissionAction;
import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.settings.permission.ModulePermissionEntity;
import com.rustam.modern_dentistry.dao.entity.settings.permission.Permission;
import com.rustam.modern_dentistry.dao.entity.users.Admin;
import com.rustam.modern_dentistry.dao.repository.BaseUserRepository;
import com.rustam.modern_dentistry.dao.repository.settings.PermissionRepository;
import com.rustam.modern_dentistry.dto.ModulePermission;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
@RequiredArgsConstructor
public class ModernDentistryApplication implements CommandLineRunner {

	private final BaseUserRepository baseUserRepository;
	private final PasswordEncoder passwordEncoder;
	private final PermissionRepository permissionRepository;

	public static void main(String[] args) {
		SpringApplication.run(ModernDentistryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		boolean existsBaseUserByEmail = baseUserRepository.existsBaseUserByEmail("superadmin@example.com");
		if (!existsBaseUserByEmail) {
			Permission superAdminPermission = createSuperAdminPermission();

			Admin admin = Admin.builder()
					.id(UUID.randomUUID())
					.name("Super")
					.surname("Admin")
					.phone("+994501112233")
					.email("superadmin@example.com")
					.username("super_admin")
					.password(passwordEncoder.encode("super1234"))
					.enabled(true)
					.permissions(Set.of(superAdminPermission))
					.build();

			baseUserRepository.save(admin);

			System.out.println("✅ Default SUPER_ADMIN created: superadmin@example.com / super1234");
		}
	}

	private Permission createSuperAdminPermission() {
		return permissionRepository.findByPermissionName("SUPER_ADMIN")
				.orElseGet(() -> {
					Permission permission = new Permission();
					permission.setPermissionName("SUPER_ADMIN");

					List<String> modules = List.of(
							"patient", "doctor", "appointment", "add-worker", "general-calendar",
							"patient-blacklist", "reservation", "technician", "workers-work-schedule"
					);

					List<ModulePermissionEntity> modulePermissions = new ArrayList<>();
					for (String module : modules) {
						ModulePermissionEntity modulePermissionEntity = new ModulePermissionEntity();
						modulePermissionEntity.setModuleUrl("/api/v1/" + module + "/**");
						modulePermissionEntity.setPermission(permission);

						modulePermissionEntity.setActions(EnumSet.allOf(PermissionAction.class));

						modulePermissions.add(modulePermissionEntity);
					}
					permission.setModulePermissions(modulePermissions);

					return permissionRepository.save(permission);
				});
	}

}
