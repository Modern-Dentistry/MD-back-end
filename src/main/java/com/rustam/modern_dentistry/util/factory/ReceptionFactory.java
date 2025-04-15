package com.rustam.modern_dentistry.util.factory;

import com.rustam.modern_dentistry.dao.entity.users.Reception;
import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.repository.ReceptionRepository;
import com.rustam.modern_dentistry.dto.request.create.AddWorkerCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.AddWorkerUpdateRequest;
import com.rustam.modern_dentistry.exception.custom.UserNotFountException;
import com.rustam.modern_dentistry.util.factory.field_util.FieldSetter;
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
                .phone2(addWorkerCreateRequest.getPhone2())
                .phone3(addWorkerCreateRequest.getPhone3())
                .degree(addWorkerCreateRequest.getDegree())
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
    public void updateUser(AddWorkerUpdateRequest request) {
        Reception reception = receptionRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFountException("No such Reception found."));
        FieldSetter.setIfNotBlank(request.getName(), reception::setName);
        FieldSetter.setIfNotBlank(request.getSurname(), reception::setSurname);
        FieldSetter.setIfNotBlank(request.getPatronymic(), reception::setPatronymic);
        FieldSetter.setIfNotBlank(request.getUsername(), reception::setUsername);
        FieldSetter.setIfNotBlank(request.getAddress(), reception::setAddress);
        FieldSetter.setIfNotNull(request.getExperience(), reception::setExperience);
        FieldSetter.setIfNotNull(request.getDateOfBirth(), reception::setDateOfBirth);
        FieldSetter.setIfNotBlank(request.getFinCode(), reception::setFinCode);
        FieldSetter.setIfNotBlank(request.getHomePhone(), reception::setHomePhone);
        FieldSetter.setIfNotNull(request.getGenderStatus(), reception::setGenderStatus);
        FieldSetter.setIfNotEmpty(request.getAuthorities(), reception::setAuthorities);
        FieldSetter.setIfNotBlank(request.getEmail(), reception::setEmail);
        FieldSetter.setIfNotBlank(request.getDegree(), reception::setDegree);
        FieldSetter.setIfNotBlank(request.getPhone(), reception::setPhone);
        FieldSetter.setIfNotBlank(request.getPhone2(), reception::setPhone2);
        FieldSetter.setIfNotBlank(request.getPhone3(), reception::setPhone3);
        FieldSetter.setIfNotBlank(request.getPassword(),
                pass -> reception.setPassword(passwordEncoder.encode(pass)));
        reception.setEnabled(true);
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
