package com.rustam.modern_dentistry.dao.entity.settings.teeth;

import com.rustam.modern_dentistry.dao.entity.Examination;
import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "teeth_examination")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TeethExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "tooth_no")
    Long toothNo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tooth_id", nullable = false)
    Teeth teeth;

    @Enumerated(EnumType.STRING)
    Status status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "examination_id", nullable = false)
    Examination examination;
}
