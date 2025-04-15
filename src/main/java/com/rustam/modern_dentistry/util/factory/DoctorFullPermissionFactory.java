package com.rustam.modern_dentistry.util.factory;

import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.repository.DoctorRepository;
import com.rustam.modern_dentistry.dto.request.create.AddWorkerCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.AddWorkerUpdateRequest;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.UserNotFountException;
import com.rustam.modern_dentistry.util.UtilService;
import com.rustam.modern_dentistry.util.factory.field_util.FieldSetter;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DoctorFullPermissionFactory implements UserRoleFactory {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilService utilService;

    @Override
    public void createUser(AddWorkerCreateRequest addWorkerCreateRequest) {
        boolean existsByUsernameAndEmailAndFinCodeAndColorCode = utilService.existsByUsernameAndEmailAndFinCodeAndColorCode(addWorkerCreateRequest.getUsername(), addWorkerCreateRequest.getEmail(),
                addWorkerCreateRequest.getFinCode(), null
        );
        if (existsByUsernameAndEmailAndFinCodeAndColorCode){
            throw new ExistsException("bu fieldlar database-de movcuddur");
        }
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
    public void updateUser(AddWorkerUpdateRequest request) {
        boolean existsByUsernameAndEmailAndFinCodeAndColorCode = utilService.existsByUsernameAndEmailAndFinCodeAndColorCode(request.getUsername(), request.getEmail(),
                request.getFinCode(), null
        );
        if (existsByUsernameAndEmailAndFinCodeAndColorCode){
            throw new ExistsException("bu fieldlar database-de movcuddur");
        }
        Doctor doctor = doctorRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFountException("No such Doctor found."));

        FieldSetter.setIfNotBlank(request.getName(), doctor::setName);
        FieldSetter.setIfNotBlank(request.getSurname(), doctor::setSurname);
        FieldSetter.setIfNotBlank(request.getPatronymic(), doctor::setPatronymic);
        FieldSetter.setIfNotBlank(request.getUsername(), doctor::setUsername);
        FieldSetter.setIfNotBlank(request.getAddress(), doctor::setAddress);
        FieldSetter.setIfNotNull(request.getExperience(), doctor::setExperience);
        FieldSetter.setIfNotNull(request.getDateOfBirth(), doctor::setDateOfBirth);
        FieldSetter.setIfNotBlank(request.getFinCode(), doctor::setFinCode);
        FieldSetter.setIfNotBlank(request.getHomePhone(), doctor::setHomePhone);
        FieldSetter.setIfNotNull(request.getGenderStatus(), doctor::setGenderStatus);
        FieldSetter.setIfNotEmpty(request.getAuthorities(), doctor::setAuthorities);
        FieldSetter.setIfNotBlank(request.getEmail(), doctor::setEmail);
        FieldSetter.setIfNotBlank(request.getDegree(), doctor::setDegree);
        FieldSetter.setIfNotBlank(request.getPhone(), doctor::setPhone);
        FieldSetter.setIfNotBlank(request.getPhone2(), doctor::setPhone2);
        FieldSetter.setIfNotBlank(request.getPhone3(), doctor::setPhone3);
        FieldSetter.setIfNotBlank(request.getPassword(),
                pass -> doctor.setPassword(passwordEncoder.encode(pass)));
        doctor.setEnabled(true);
        doctorRepository.save(doctor);
    }

    @Override
    public void deleteUser(UUID id) {
        doctorRepository.deleteById(id);
    }

    @Override
    public Role getRole() {
        return Role.DOCTOR_FULL_PERMISSION;
    }
}
