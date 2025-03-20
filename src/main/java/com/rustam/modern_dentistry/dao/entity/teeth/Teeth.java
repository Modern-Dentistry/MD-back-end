package com.rustam.modern_dentistry.dao.entity.teeth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rustam.modern_dentistry.dao.entity.Examination;
import com.rustam.modern_dentistry.dao.entity.enums.status.ToothLocation;
import com.rustam.modern_dentistry.dao.entity.enums.status.ToothType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "teeth")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Teeth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long toothNo;

    @Enumerated(EnumType.STRING)
    ToothType toothType;

    @Enumerated(EnumType.STRING)
    ToothLocation toothLocation;

    @OneToMany(mappedBy = "teeth", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<TeethExamination> toothExaminations = new ArrayList<>();
}
