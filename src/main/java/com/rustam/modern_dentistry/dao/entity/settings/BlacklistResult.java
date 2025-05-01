package com.rustam.modern_dentistry.dao.entity.settings;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "blacklist_results")
@FieldDefaults(level = PRIVATE)
public class BlacklistResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String statusName;
}
