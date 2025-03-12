package com.rustam.modern_dentistry.util;

import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dao.entity.users.Reception;
import com.rustam.modern_dentistry.dao.repository.BaseUserRepository;
import com.rustam.modern_dentistry.dao.repository.DoctorRepository;
import com.rustam.modern_dentistry.dao.repository.PatientRepository;
import com.rustam.modern_dentistry.dao.repository.ReceptionRepository;
import com.rustam.modern_dentistry.dto.response.TokenPair;
import com.rustam.modern_dentistry.exception.custom.DoctorNotFoundException;
import com.rustam.modern_dentistry.exception.custom.InvalidUUIDFormatException;
import com.rustam.modern_dentistry.exception.custom.UserNotFountException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class UtilService {

    DoctorRepository doctorRepository;
    PatientRepository patientRepository;
    BaseUserRepository baseUserRepository;
    JwtUtil jwtUtil;

    public Patient findByPatientId(Long userId){
        return patientRepository.findById(userId)
                .orElseThrow(() -> new UserNotFountException("No such patient found."));
    }

    public boolean existsByUsernameExists(String username) {
       return doctorRepository.existsByUsername(username);
    }

    public UUID convertToUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDFormatException("Invalid UUID format for ID: " + id, e);
        }
    }

    public BaseUser findByUsername(String username) {
       return baseUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No such username found."));
    }

    public TokenPair tokenProvider(String id, UserDetails userDetails) {
        return userDetails.isEnabled() ?
                TokenPair.builder()
                        .accessToken(jwtUtil.createToken(String.valueOf(id)))
                        .refreshToken(jwtUtil.createRefreshToken(String.valueOf(id)))
                        .build()
                : new TokenPair();
    }

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    public BaseUser findByBaseUserId(String currentUserId) {
        return baseUserRepository.findById(UUID.fromString(currentUserId))
                .orElseThrow(() -> new UserNotFountException("No such user found."));
    }

    public List<Patient> findByDoctorIdWithPatients(UUID doctorId) {
        return patientRepository.findAllByDoctor_Id(doctorId);
    }
}
