package com.rustam.modern_dentistry.service.patient_info;

import com.rustam.modern_dentistry.dao.entity.patient_info.PatientInsuranceBalance;
import com.rustam.modern_dentistry.dao.repository.patient_info.PatientInsuranceBalanceRepository;
import com.rustam.modern_dentistry.dto.request.create.PatInsuranceBalanceCreateReq;
import com.rustam.modern_dentistry.dto.request.update.PatInsuranceBalanceUpdateReq;
import com.rustam.modern_dentistry.dto.response.read.PatInsuranceBalanceReadResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.patient_info.PatientInsuranceBalanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.ACTIVE;
import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.PASSIVE;

@Service
@RequiredArgsConstructor
public class PatientInsuranceBalanceService {
    private final PatientInsuranceService patientInsuranceService;
    private final PatientInsuranceBalanceMapper patientInsuranceBalanceMapper;
    private final PatientInsuranceBalanceRepository patientInsuranceBalanceRepository;

//    @Value("${file.path.pat-insurance-balance}")
//    private String uploadDir;

    public void create(PatInsuranceBalanceCreateReq request) {
        checkDate(request.getDate(), request.getPatientInsuranceId());
        var patientInsurance = patientInsuranceService.getPatientInsuranceById(request.getPatientInsuranceId());
        var entity = patientInsuranceBalanceMapper.toEntity(request);
        entity.setPatientInsurance(patientInsurance);
        patientInsuranceBalanceRepository.save(entity);
    }

    public List<PatInsuranceBalanceReadResponse> read(Long patientInsuranceId) {
        var patientInsurances = patientInsuranceBalanceRepository.findAllByPatientInsurance_Id(patientInsuranceId);
        return patientInsurances.stream()
                .map(patientInsuranceBalanceMapper::toReadDto)
                .toList();
    }

    public PatInsuranceBalanceReadResponse readById(Long id) {
        var patientInsurance = getPatientInsurance(id);
        return patientInsuranceBalanceMapper.toReadDto(patientInsurance);
    }

    public void update(Long id, PatInsuranceBalanceUpdateReq request) {
        checkDate(request.getDate(), request.getPatientInsuranceId());
        var patientInsurance = getPatientInsurance(id);
        patientInsuranceBalanceMapper.update(patientInsurance, request);
        patientInsuranceBalanceRepository.save(patientInsurance);
    }

    public void updateStatus(Long id) {
        var patientInsurance = getPatientInsurance(id);
        patientInsurance.setStatus(patientInsurance.getStatus() == ACTIVE ? PASSIVE : ACTIVE);
        patientInsuranceBalanceRepository.save(patientInsurance);
    }

    public void delete(Long id) {
        var patientInsurance = getPatientInsurance(id);
        patientInsuranceBalanceRepository.delete(patientInsurance);
    }

    private PatientInsuranceBalance getPatientInsurance(Long id) {
        return patientInsuranceBalanceRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də qalıq məbləği tapımadı:" + id)
        );
    }

    private void checkDate(LocalDate date, Long patientInsuranceId) {
        var result = patientInsuranceBalanceRepository.existsByDateAndPatientInsurance_Id(date, patientInsuranceId);
        if (result) throw new ExistsException("Bu tarix artıq əlavə edilib.");
    }
}

//    public void create(PatInsuranceBalanceCreateReq request, MultipartFile file) {
//        checkDate(request.getDate());
//        var patientInsurance = patientInsuranceService.getPatientInsuranceById(request.getPatientInsuranceId());
//        var entity = patientInsuranceBalanceMapper.toEntity(request);
//        var path = writeFile(file, uploadDir);
//        System.out.println("path : " + path);
//        entity.setPatientInsurance(patientInsurance);
//        entity.setUrl(file.getOriginalFilename());
//
//        patientInsuranceBalanceRepository.save(entity);
//    }
//
//    public InputStreamResource getFile(String fileName) {
//        Path targetLocation = Paths.get(uploadDir).resolve(fileName).normalize();
//        File file = targetLocation.toFile();
//        if (!file.exists()) throw new NotFoundException("Fayl tapılmadı: " + fileName);
//        try {
//            InputStream inputStream = Files.newInputStream(targetLocation);
//            return new InputStreamResource(inputStream);
//        } catch (IOException e) {
//            throw new RuntimeException("Fayl oxunarkən xəta baş verdi: " + fileName, e);
//        }
//    }
//
//
//    public String writeFile(MultipartFile file, String uploadDir) {
//        try {
//            String fileName = file.getOriginalFilename();
//            Path targetLocation = Paths.get(uploadDir).resolve(fileName);
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return targetLocation.toAbsolutePath().normalize().toString();
//        } catch (IOException e) {
//            throw new RuntimeException("File yüklənə bilmədi"); // FileOperationException
//        }
//    }