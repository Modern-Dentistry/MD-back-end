package com.rustam.modern_dentistry.dao.repository;


import com.rustam.modern_dentistry.dao.entity.GeneralCalendar;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface GeneralCalendarRepository extends JpaRepository<GeneralCalendar,Long> {
    @Query("SELECT g FROM GeneralCalendar g WHERE g.doctor.id = :doctorId")
    List<GeneralCalendar> findAllByDoctor_Id(UUID doctorId);
}
