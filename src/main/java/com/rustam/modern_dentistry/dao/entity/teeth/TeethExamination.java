package com.rustam.modern_dentistry.dao.entity.teeth;

import com.rustam.modern_dentistry.dao.entity.Examination;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tooth_id", nullable = false)
    Teeth teeth;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "examination_id", nullable = false)
    Examination examination;
}
