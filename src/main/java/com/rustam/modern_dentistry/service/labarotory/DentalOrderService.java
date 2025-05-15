package com.rustam.modern_dentistry.service.labarotory;

import com.rustam.modern_dentistry.dao.entity.laboratory.DentalOrder;
import com.rustam.modern_dentistry.dao.entity.laboratory.DentalOrderToothDetail;
import com.rustam.modern_dentistry.dao.repository.laboratory.DentalOrderRepository;
import com.rustam.modern_dentistry.dto.request.DentalOrderCreateReq;
import com.rustam.modern_dentistry.mapper.laboratory.DentalOrderMapper;
import com.rustam.modern_dentistry.service.DoctorService;
import com.rustam.modern_dentistry.service.FileService;
import com.rustam.modern_dentistry.service.TechnicianService;
import com.rustam.modern_dentistry.service.settings.teeth.TeethService;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.rustam.modern_dentistry.util.constants.Directory.pathDentalOrder;

@Service
@RequiredArgsConstructor
public class DentalOrderService {
    private final UtilService utilService;
    private final FileService fileService;
    private final TeethService teethService;
    private final DoctorService doctorService;
    private final TechnicianService technicianService;
    private final DentalOrderMapper dentalOrderMapper;
    private final DentalOrderRepository dentalOrderRepository;

    @Transactional
    public void create(DentalOrderCreateReq request, List<MultipartFile> files) {
        List<String> imagePaths = new ArrayList<>();
        try {
            var entity = dentalOrderMapper.toEntity(request);
            var teeth = teethService.findAllById(request.getTeethList());
            var doctor = doctorService.findById(request.getDoctorId());
            var technician = technicianService.getTechnicianById(request.getTechnicianId());
            var patient = utilService.findByPatientId(request.getPatientId());
            entity.setTeethList(teeth);
            entity.setDoctor(doctor);
            entity.setTechnician(technician);
            entity.setPatient(patient);
//            entity.setOrderDentureInfo(request.getOrderDentureInfo());
            entity.setToothDetails(getToothDetails(request, entity));
//        fileService.checkVideoFile(file);
            files.forEach(file -> {
                System.out.println("name: " + file.getOriginalFilename());
                System.out.println("content/type: " + file.getContentType());
                System.out.println("size: " + file.getSize());
                System.out.println("\n");
                var newFileName = fileService.getNewFileName(file,
                        patient.getName() + "_"
                        + patient.getSurname() + "_"
                        + patient.getPatronymic() + "_"
                        + "dental_order" + "_");
                imagePaths.add(newFileName);
                fileService.writeFile(file, pathDentalOrder, newFileName);
            });
            entity.setImagePaths(imagePaths);
            dentalOrderRepository.save(entity);
        } catch (Exception e) {
            // if error happens remove files
            imagePaths.forEach(path -> {
                var fullPath = pathDentalOrder + "/" + path;
                fileService.deleteFile(fullPath);
            });
        }

    }

    private List<DentalOrderToothDetail> getToothDetails(DentalOrderCreateReq request, DentalOrder dentalOrder) {
        return request.getToothDetailIds().stream()
                .map(toothDetail -> DentalOrderToothDetail.builder()
                        .color(toothDetail.getColor())
                        .metal(toothDetail.getMetal())
                        .ceramic(toothDetail.getCeramic())
                        .dentalOrder(dentalOrder)
                        .build()).toList();
    }

}
