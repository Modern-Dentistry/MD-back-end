package com.rustam.modern_dentistry.dao.repository;

import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BaseUserRepository extends JpaRepository<BaseUser, UUID>, JpaSpecificationExecutor<BaseUser> {
    Optional<BaseUser> findByUsername(String username);

    boolean existsBaseUserByEmail(String mail);

    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END
    FROM base_users u
    LEFT JOIN doctors d ON d.doctor_id = u.id
    LEFT JOIN authorities a ON u.id = a.base_user_id
    LEFT JOIN permission p ON a.permission_id = p.id
    WHERE (u.username = :username OR u.email = :email OR u.fin_code = :finCode)
      AND (
          p.permission_name <> 'DOCTOR'
          OR (:colorCode IS NULL OR d.color_code = :colorCode)
      )
    """, nativeQuery = true)
    boolean existsUserFully(String username, String email, String finCode, String colorCode);



    @Query("""
                SELECT DISTINCT u FROM BaseUser u
                LEFT JOIN FETCH u.permissions p
                LEFT JOIN FETCH p.modulePermissions mp
                LEFT JOIN FETCH mp.actions a
                WHERE u.id = :username
            """)
    Optional<BaseUser> findUserWithAllPermissions(UUID username);

    @Query("SELECT u FROM BaseUser u LEFT JOIN FETCH u.permissions WHERE u.id = :id")
    Optional<BaseUser> findByIdWithPermissions(UUID id);

    @Query("""
    SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END
    FROM BaseUser u
    WHERE u.username = :username OR u.email = :email OR u.finCode = :finCode
    """)
    boolean existsByUsernameOrEmailOrFinCode(String username, String email, String finCode);

    @EntityGraph(attributePaths = "permissions")
    Optional<BaseUser> findByUsernameOrEmailOrFinCode(@NotBlank(message = "İstifadəçi Ad boş ola bilməz") @Size(min = 3, max = 20, message = "İstifadəçi Adı 3-20 simvol arasında olmalıdır") String username, @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Enter a valid email address.") String email, @Pattern(
            regexp = "^$|^[A-Z0-9]{7}$",
            message = "FIN kod yalnız böyük hərflər və rəqəmlərdən ibarət 7 simvol olmalıdır."
    ) String finCode);

//    @Query("""
//                SELECT u FROM BaseUser u
//                LEFT JOIN FETCH u.permissions p
//                WHERE :permission IS NULL OR :permission = p.permissionName
//            """)
//    List<BaseUser> findAllByPermissionWithJoin(String permission);
}
