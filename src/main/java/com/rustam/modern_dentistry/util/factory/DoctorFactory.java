package com.rustam.modern_dentistry.util.factory;

import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.entity.users.Admin;
import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.repository.DoctorRepository;
import com.rustam.modern_dentistry.dto.request.create.AddWorkerCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.AddWorkerUpdateRequest;
import com.rustam.modern_dentistry.exception.custom.UserNotFountException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DoctorFactory implements UserRoleFactory {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(AddWorkerCreateRequest addWorkerCreateRequest) {
        Doctor doctor = Doctor.builder()
                .username(addWorkerCreateRequest.getUsername())
                .password(passwordEncoder.encode(addWorkerCreateRequest.getPassword()))
                .name(addWorkerCreateRequest.getName())
                .surname(addWorkerCreateRequest.getSurname())
                .patronymic(addWorkerCreateRequest.getPatronymic())
                .finCode(addWorkerCreateRequest.getFinCode())
                .colorCode(addWorkerCreateRequest.getColorCode())
                .genderStatus(addWorkerCreateRequest.getGenderStatus())
                .dateOfBirth(addWorkerCreateRequest.getDateOfBirth())
                .degree(addWorkerCreateRequest.getDegree())
                .phone(addWorkerCreateRequest.getPhone())
                .phone2(addWorkerCreateRequest.getPhone2())
                .homePhone(addWorkerCreateRequest.getHomePhone())
                .email(addWorkerCreateRequest.getEmail())
                .address(addWorkerCreateRequest.getAddress())
                .experience(addWorkerCreateRequest.getExperience())
                .authorities(addWorkerCreateRequest.getAuthorities())
                .enabled(true)
                .build();
        doctorRepository.save(doctor);
    }

    @Override
    public void updateUser(AddWorkerUpdateRequest addWorkerUpdateRequest) {
        Doctor doctor = doctorRepository.findById(addWorkerUpdateRequest.getId())
                .orElseThrow(() -> new UserNotFountException("No such Doctor found."));
        doctor.setName(addWorkerUpdateRequest.getName());
        doctor.setSurname(addWorkerUpdateRequest.getSurname());
        doctor.setPatronymic(addWorkerUpdateRequest.getPatronymic());
        doctor.setUsername(addWorkerUpdateRequest.getUsername());
        doctor.setAddress(addWorkerUpdateRequest.getAddress());
        doctor.setExperience(addWorkerUpdateRequest.getExperience());
        doctor.setDateOfBirth(addWorkerUpdateRequest.getDateOfBirth());
        doctor.setFinCode(addWorkerUpdateRequest.getFinCode());
        doctor.setHomePhone(addWorkerUpdateRequest.getHomePhone());
        doctor.setGenderStatus(addWorkerUpdateRequest.getGenderStatus());
        doctor.setAuthorities(addWorkerUpdateRequest.getAuthorities());
        doctor.setEmail(addWorkerUpdateRequest.getEmail());
        doctor.setDegree(addWorkerUpdateRequest.getDegree());
        doctor.setColorCode(addWorkerUpdateRequest.getColorCode());
        doctor.setEnabled(true);
        doctor.setPassword(passwordEncoder.encode(addWorkerUpdateRequest.getPassword()));
        doctor.setPhone(addWorkerUpdateRequest.getPhone());
        doctorRepository.save(doctor);
    }

    @Override
    public void deleteUser(UUID id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public Role getRole() {
        return Role.DOCTOR;
    }
}
