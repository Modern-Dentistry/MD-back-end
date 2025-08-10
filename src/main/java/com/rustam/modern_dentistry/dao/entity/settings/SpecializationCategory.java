package com.rustam.modern_dentistry.dao.entity.settings;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "specialization_categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SpecializationCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @Enumerated(EnumType.STRING)
    Status status;
    @OneToMany(mappedBy = "specializationCategory",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Patient> patients;
}
