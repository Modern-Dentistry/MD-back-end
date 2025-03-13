package com.rustam.modern_dentistry.dao.entity.users;

import com.rustam.modern_dentistry.dao.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "base_users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type",discriminatorType = DiscriminatorType.STRING)
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
    @Column(unique = true)
    String email;
    String username;
    String password;
    Boolean enabled;
    @Column(name = "user_type", insertable = false, updatable = false)
    String userType;

    @ElementCollection(targetClass = Role.class)
    @JoinTable(name = "authorities", joinColumns = @JoinColumn(name = "base_user_id"))
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    Set<Role> authorities;

    public String getId() {
        if (id != null) {
            return id.toString();
        }
        return null;
    }
}
