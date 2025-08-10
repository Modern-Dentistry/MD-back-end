package com.rustam.modern_dentistry.dao.entity;

import com.rustam.modern_dentistry.dao.entity.enums.WeekDay;
import com.rustam.modern_dentistry.dao.entity.enums.status.Room;
import com.rustam.modern_dentistry.dao.entity.settings.Cabinet;
import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;

@Entity
@Table(name = "workers_work_schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkersWorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    WeekDay weekDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "worker_id", nullable = false)
    BaseUser worker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cabinet_id")
    Cabinet cabinet;

    LocalTime startTime;

    LocalTime finishTime;
}
