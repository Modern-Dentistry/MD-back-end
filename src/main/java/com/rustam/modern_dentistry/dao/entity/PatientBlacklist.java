package com.rustam.modern_dentistry.dao.entity;

import com.rustam.modern_dentistry.dao.entity.settings.BlacklistResult;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patient_blacklists")
@FieldDefaults(level = PRIVATE)
public class PatientBlacklist {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @OneToMany(fetch = LAZY)
    List<BlacklistResult> blacklistResultList;

    @ManyToOne(fetch = LAZY)
    Patient patient;
}
