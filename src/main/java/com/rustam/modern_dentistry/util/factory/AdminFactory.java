package com.rustam.modern_dentistry.util.factory;

import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.entity.users.Admin;
import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import com.rustam.modern_dentistry.dao.repository.AdminRepository;
import com.rustam.modern_dentistry.dao.repository.DoctorRepository;
import com.rustam.modern_dentistry.dto.request.create.AddWorkerCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.AddWorkerUpdateRequest;
import com.rustam.modern_dentistry.exception.custom.UserNotFountException;
import com.rustam.modern_dentistry.util.UtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AdminFactory implements UserRoleFactory {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final UtilService utilService;

    @Override
    public void createUser(AddWorkerCreateRequest addWorkerCreateRequest) {
        Admin admin = Admin.builder()
                .username(addWorkerCreateRequest.getUsername())
                .password(passwordEncoder.encode(addWorkerCreateRequest.getPassword()))
                .name(addWorkerCreateRequest.getName())
                .surname(addWorkerCreateRequest.getSurname())
                .patronymic(addWorkerCreateRequest.getPatronymic())
                .finCode(addWorkerCreateRequest.getFinCode())
                .genderStatus(addWorkerCreateRequest.getGenderStatus())
                .dateOfBirth(addWorkerCreateRequest.getDateOfBirth())
                .phone(addWorkerCreateRequest.getPhone())
                .email(addWorkerCreateRequest.getEmail())
                .address(addWorkerCreateRequest.getAddress())
                .experience(addWorkerCreateRequest.getExperience())
                .authorities(addWorkerCreateRequest.getAuthorities())
                .enabled(true)
                .build();
        adminRepository.save(admin);
    }



    @Override
    public Role getRole() {
        return Role.ADMIN;
    }

    @Override
    public void updateUser(AddWorkerUpdateRequest addWorkerUpdateRequest) {
        Admin admin = adminRepository.findById(addWorkerUpdateRequest.getId())
                .orElseThrow(() -> new UserNotFountException("No such Admin found."));
        admin.setName(addWorkerUpdateRequest.getName());
        admin.setSurname(addWorkerUpdateRequest.getSurname());
        admin.setPatronymic(addWorkerUpdateRequest.getPatronymic());
        admin.setUsername(addWorkerUpdateRequest.getUsername());
        admin.setAddress(addWorkerUpdateRequest.getAddress());
        admin.setExperience(addWorkerUpdateRequest.getExperience());
        admin.setDateOfBirth(addWorkerUpdateRequest.getDateOfBirth());
        admin.setFinCode(addWorkerUpdateRequest.getFinCode());
        admin.setHomePhone(addWorkerUpdateRequest.getHomePhone());
        admin.setGenderStatus(addWorkerUpdateRequest.getGenderStatus());
        admin.setAuthorities(addWorkerUpdateRequest.getAuthorities());
        admin.setEmail(addWorkerUpdateRequest.getEmail());
        admin.setEnabled(true);
        admin.setPassword(passwordEncoder.encode(addWorkerUpdateRequest.getPassword()));
        admin.setPhone(addWorkerUpdateRequest.getPhone());
        adminRepository.save(admin);
    }

    @Override
    public void deleteUser(UUID id) {
        adminRepository.deleteById(id);
    }

}
