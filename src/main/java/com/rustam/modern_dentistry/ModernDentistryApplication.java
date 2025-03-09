package com.rustam.modern_dentistry;

import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.entity.users.Admin;
import com.rustam.modern_dentistry.dao.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@RequiredArgsConstructor
public class ModernDentistryApplication implements CommandLineRunner {

	private final BaseUserRepository baseUserRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(ModernDentistryApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		boolean existsBaseUserByEmail = baseUserRepository.existsBaseUserByEmail("admin@example.com");
		if (!existsBaseUserByEmail) {
			Admin admin = Admin.builder()
					.id(UUID.randomUUID())
					.name("Admin")
					.surname("User")
					.phone("+123456789")
					.email("admin@example.com")
					.username("admin_1")
					.password(passwordEncoder.encode("admin123")) // Şifrəni encode et!
					.enabled(true)
					.authorities(Set.of(Role.ADMIN))
					.build();
			baseUserRepository.save(admin);
			System.out.println("Default admin created: admin@example.com / admin123");
		}
	}
}
