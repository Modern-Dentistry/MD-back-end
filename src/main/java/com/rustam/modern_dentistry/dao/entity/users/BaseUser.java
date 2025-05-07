package com.rustam.modern_dentistry.dao.entity.users;

import com.rustam.modern_dentistry.dao.entity.WorkersWorkSchedule;
import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.entity.enums.status.GenderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "base_users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseUser {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    UUID id;
    String name;
    String surname;
    String phone;
    String email;
    @Column(unique = true, name = "fin_code")
    String finCode;
    String username;
    String patronymic;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_status")
    GenderStatus genderStatus;
    @Column(name = "date_of_birth")
    LocalDate dateOfBirth;
    String password;
    Boolean enabled;
    @Column(name = "user_type", insertable = false, updatable = false)
    String userType;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "authorities", joinColumns = @JoinColumn(name = "base_user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    Set<Role> authorities;

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = LAZY)
    Set<WorkersWorkSchedule> workSchedules;

    public String getId() {
        if (id != null) {
            return id.toString();
        }
        return null;
    }
}
