package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface BaseUserRepository extends JpaRepository<BaseUser, UUID>, JpaSpecificationExecutor<BaseUser> {
    Optional<BaseUser> findByUsername(String username);
    boolean existsBaseUserByEmail(String mail);

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
    FROM base_users u
    LEFT JOIN doctors d ON d.doctor_id = u.id
    WHERE u.username = :username
      OR u.email = :email
      OR u.fin_code = :finCode
      AND (
          u.user_type <> 'Doctor'
          OR (:colorCode IS NULL OR d.color_code = :colorCode)
      )
""", nativeQuery = true)
    boolean existsUserFully(String username, String email, String finCode, String colorCode);
}
