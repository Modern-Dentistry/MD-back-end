package com.rustam.modern_dentistry.util;

import com.rustam.modern_dentistry.dao.repository.PatientRepository;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValidationUtilService {

    PatientRepository patientRepository;

    public void validateUniqueFields(String email, String finCode) {
        if (email != null && !email.isBlank()) {
            if (patientRepository.existsByEmail(email)) {
                throw new ExistsException("Bu email artıq mövcuddur");
            }
        }

        // FIN kod yoxlaması
        if (finCode != null && !finCode.isBlank()) {
            if (patientRepository.existsByFinCode(finCode)) {
                throw new ExistsException("Bu FIN kod artıq mövcuddur");
            }
        }
    }
}
