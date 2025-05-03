package com.rustam.modern_dentistry.dao.entity.settings;

import com.rustam.modern_dentistry.dao.entity.PatientBlacklist;
import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.ACTIVE;
import static jakarta.persistence.FetchType.LAZY;
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
    Status status;

    @OneToMany(mappedBy = "blacklistResult", cascade = CascadeType.REMOVE)
    List<PatientBlacklist> patientBlacklist;

    @PrePersist
    public void prePersist() {
        status = ACTIVE;
    }
}
