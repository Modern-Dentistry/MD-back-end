package com.rustam.modern_dentistry.dao.entity.users;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "technician")
@DiscriminatorValue("TECHNICIAN")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "technician_id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Technician extends BaseUser {
    String phone2;
    String homePhone;
    String address;
    //    List<Permission> permissions
}
