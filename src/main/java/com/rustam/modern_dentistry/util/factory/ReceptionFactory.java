package com.rustam.modern_dentistry.util.factory;

import com.rustam.modern_dentistry.dao.entity.users.Reception;
import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.repository.ReceptionRepository;
import com.rustam.modern_dentistry.dto.request.create.AddWorkerCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.AddWorkerUpdateRequest;
import com.rustam.modern_dentistry.exception.custom.UserNotFountException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReceptionFactory implements UserRoleFactory {

    private final ReceptionRepository receptionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(AddWorkerCreateRequest addWorkerCreateRequest) {
        Reception reception = Reception.builder()
                .username(addWorkerCreateRequest.getUsername())
                .password(passwordEncoder.encode(addWorkerCreateRequest.getPassword()))
                .name(addWorkerCreateRequest.getName())
                .surname(addWorkerCreateRequest.getSurname())
                .patronymic(addWorkerCreateRequest.getPatronymic())
                .finCode(addWorkerCreateRequest.getFinCode())
                .genderStatus(addWorkerCreateRequest.getGenderStatus())
                .dateOfBirth(addWorkerCreateRequest.getDateOfBirth())
                .phone(addWorkerCreateRequest.getPhone())
                .homePhone(addWorkerCreateRequest.getHomePhone())
                .email(addWorkerCreateRequest.getEmail())
                .address(addWorkerCreateRequest.getAddress())
                .experience(addWorkerCreateRequest.getExperience())
                .authorities(addWorkerCreateRequest.getAuthorities())
                .enabled(true)
                .build();
        receptionRepository.save(reception);
    }
    @Override
    public void updateUser(AddWorkerUpdateRequest addWorkerUpdateRequest) {
        Reception reception = receptionRepository.findById(addWorkerUpdateRequest.getId())
                .orElseThrow(() -> new UserNotFountException("No such Reception found."));
        reception.setName(addWorkerUpdateRequest.getName());
        reception.setSurname(addWorkerUpdateRequest.getSurname());
        reception.setPatronymic(addWorkerUpdateRequest.getPatronymic());
        reception.setUsername(addWorkerUpdateRequest.getUsername());
        reception.setAddress(addWorkerUpdateRequest.getAddress());
        reception.setExperience(addWorkerUpdateRequest.getExperience());
        reception.setDateOfBirth(addWorkerUpdateRequest.getDateOfBirth());
        reception.setFinCode(addWorkerUpdateRequest.getFinCode());
        reception.setHomePhone(addWorkerUpdateRequest.getHomePhone());
        reception.setGenderStatus(addWorkerUpdateRequest.getGenderStatus());
        reception.setAuthorities(addWorkerUpdateRequest.getAuthorities());
        reception.setEmail(addWorkerUpdateRequest.getEmail());
        reception.setEnabled(true);
        reception.setPassword(passwordEncoder.encode(addWorkerUpdateRequest.getPassword()));
        reception.setPhone(addWorkerUpdateRequest.getPhone());
        receptionRepository.save(reception);
    }

    @Override
    public void deleteUser(UUID id) {
        receptionRepository.deleteById(id);
    }

    @Override
    public Role getRole() {
        return Role.RECEPTION;
    }
}
