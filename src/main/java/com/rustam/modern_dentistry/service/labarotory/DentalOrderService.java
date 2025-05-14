package com.rustam.modern_dentistry.service.labarotory;

import com.rustam.modern_dentistry.dao.repository.laboratory.DentalOrderRepository;
import com.rustam.modern_dentistry.dto.request.DentalOrderCreateReq;
import com.rustam.modern_dentistry.mapper.laboratory.DentalOrderMapper;
import com.rustam.modern_dentistry.service.DoctorService;
import com.rustam.modern_dentistry.service.PatientService;
import com.rustam.modern_dentistry.service.TechnicianService;
import com.rustam.modern_dentistry.service.settings.teeth.TeethService;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DentalOrderService {
    private final TeethService teethService;
    private final DentalOrderMapper dentalOrderMapper;
    private final DentalOrderRepository dentalOrderRepository;
    private final UtilService utilService;
    private final DoctorService doctorService;
    private final TechnicianService technicianService;
    private final PatientService patientService;

    public void create(DentalOrderCreateReq request) {
        var entity = dentalOrderMapper.toEntity(request);
        var teeth = teethService.findAllById(request.getTeethList());
        var doctor = doctorService.findById(request.getDoctorId());
        var technician = technicianService.getTechnicianById(request.getTechnicianId());
        var patient = utilService.findByPatientId(request.getPatientId());
        entity.setTeethList(teeth);
        entity.setDoctor(doctor);
        entity.setTechnician(technician);
        entity.setPatient(patient);

        dentalOrderRepository.save(entity);
    }
}
