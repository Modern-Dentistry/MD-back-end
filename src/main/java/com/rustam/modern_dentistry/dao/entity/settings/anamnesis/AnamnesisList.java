package com.rustam.modern_dentistry.dao.entity.settings.anamnesis;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "anamnesis_list")
@FieldDefaults(level = PRIVATE)
public class AnamnesisList {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String name;
    Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(referencedColumnName = "id", name = "category_id")
    AnamnesisCategory anamnesisCategory;
}
