package com.rustam.modern_dentistry.dao.entity;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.teeth.Teeth;
import com.rustam.modern_dentistry.dao.entity.teeth.TeethExamination;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "examinations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Examination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String typeName;

    @Enumerated(EnumType.STRING)
    Status status;
}
