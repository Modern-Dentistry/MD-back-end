package com.rustam.modern_dentistry.dao.entity.laboratory;

import com.rustam.modern_dentistry.dao.entity.enums.DentalOrderType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dental_order ")
public class DentalOrder {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;
    LocalDate checkDate;
    LocalDate deliveryDate;
    String description;
    String color;
    String garniture;

    @Enumerated(EnumType.STRING)
    DentalOrderType orderType;

    @Enumerated(EnumType.STRING)
    DentalOrderStatus orderStatus;

    @OneToMany(mappedBy = "dentalOrder", cascade = ALL)
    List<DentalOrderToothDetail> toothDetails;

    String doctor;
    String technician;
    String patient;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "doctor_id")
//    Doctor doctor;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "technician_id")
//    Technician technician;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "patient_id")
//    Patient patient;
}
