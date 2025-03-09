package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BaseUserRepository extends JpaRepository<BaseUser, UUID> {
    Optional<BaseUser> findByUsername(String username);

    boolean existsBaseUserByEmail(String mail);
}
