package com.rustam.modern_dentistry.service;

import com.rustam.modern_dentistry.dao.entity.enums.Role;
import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import com.rustam.modern_dentistry.dao.entity.users.Doctor;
import com.rustam.modern_dentistry.dao.entity.users.Reception;
import com.rustam.modern_dentistry.dao.repository.BaseUserRepository;
import com.rustam.modern_dentistry.dto.request.create.AddWorkerCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.AddWorkerSearchRequest;
import com.rustam.modern_dentistry.dto.request.update.AddWorkerUpdateRequest;
import com.rustam.modern_dentistry.dto.response.create.AddWorkerCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.AddWorkerReadResponse;
import com.rustam.modern_dentistry.dto.response.read.AddWorkerReadStatusResponse;
import com.rustam.modern_dentistry.dto.response.update.AddWorkerUpdateResponse;
import com.rustam.modern_dentistry.exception.custom.UserNotFountException;
import com.rustam.modern_dentistry.mapper.AddWorkerMapper;
import com.rustam.modern_dentistry.util.UtilService;
import com.rustam.modern_dentistry.util.factory.UserRoleFactory;
import com.rustam.modern_dentistry.util.specification.UserSpecification;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AddWorkerService {

    Map<Role, UserRoleFactory> roleFactories;
    UtilService utilService;
    BaseUserRepository baseUserRepository;
    DoctorService doctorService;
    ReceptionService receptionService;
    AdminService adminService;
    AddWorkerMapper addWorkerMapper;

    @Autowired
    public AddWorkerService(List<UserRoleFactory> factories, UtilService utilService, BaseUserRepository baseUserRepository, DoctorService doctorService, ReceptionService receptionService, AdminService adminService, AddWorkerMapper addWorkerMapper) {
        this.roleFactories = factories.stream().collect(Collectors.toMap(UserRoleFactory::getRole, Function.identity()));
        this.utilService = utilService;
        this.baseUserRepository = baseUserRepository;
        this.doctorService = doctorService;
        this.receptionService = receptionService;
        this.adminService = adminService;
        this.addWorkerMapper = addWorkerMapper;
    }

    public AddWorkerCreateResponse create(AddWorkerCreateRequest addWorkerCreateRequest) {
        addWorkerCreateRequest.getAuthorities().stream()
                .map(roleFactories::get)
                .filter(Objects::nonNull)
                .forEach(factory -> factory.createUser(addWorkerCreateRequest));
        return addWorkerResponse(addWorkerCreateRequest);
    }

    private AddWorkerCreateResponse addWorkerResponse(AddWorkerCreateRequest addWorkerCreateRequest) {
        return AddWorkerCreateResponse.builder()
                .username(addWorkerCreateRequest.getUsername())
                .password(addWorkerCreateRequest.getPassword())
                .name(addWorkerCreateRequest.getName())
                .surname(addWorkerCreateRequest.getSurname())
                .patronymic(addWorkerCreateRequest.getPatronymic())
                .colorCode(addWorkerCreateRequest.getColorCode())
                .email(addWorkerCreateRequest.getEmail())
                .finCode(addWorkerCreateRequest.getFinCode())
                .dateOfBirth(addWorkerCreateRequest.getDateOfBirth())
                .phone(addWorkerCreateRequest.getPhone())
                .genderStatus(addWorkerCreateRequest.getGenderStatus())
                .address(addWorkerCreateRequest.getAddress())
                .experience(addWorkerCreateRequest.getExperience())
                .degree(addWorkerCreateRequest.getDegree())
                .phone2(addWorkerCreateRequest.getPhone2())
                .homePhone(addWorkerCreateRequest.getHomePhone())
                .authorities(addWorkerCreateRequest.getAuthorities())
                .enabled(true)
                .build();
    }

    @Transactional
    public List<AddWorkerReadResponse> read() {
        String currentUserId = utilService.getCurrentUserId();
        BaseUser baseUser = utilService.findByBaseUserId(currentUserId);
        String role = baseUser.getUserType();
        List<? extends BaseUser> users = List.of();
        if ("ADMIN".equals(role)) {
            users = baseUserRepository.findAll();
        } else if ("DOCTOR".equals(role)) {
            users = doctorService.readAll();
        } else if ("RECEPTION".equals(role)) {
            users = receptionService.findAll();
        }

        return users.stream().map(this::convertToDto).toList();
    }

    private AddWorkerReadResponse convertToDto(BaseUser user) {
        AddWorkerReadResponse dto = AddWorkerReadResponse.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .enabled(user.getEnabled())
                .authorities(user.getAuthorities())
                .build();

        if (user instanceof Doctor doctor) {
            dto.setAddress(doctor.getAddress());
            dto.setDegree(doctor.getDegree());
            dto.setExperience(doctor.getExperience());
            dto.setColorCode(doctor.getColorCode());
            dto.setFinCode(doctor.getFinCode());
            dto.setDateOfBirth(doctor.getDateOfBirth());
            dto.setGenderStatus(doctor.getGenderStatus());
            dto.setHomePhone(doctor.getHomePhone());
            dto.setPhone2(doctor.getPhone2());
            dto.setPatronymic(doctor.getPatronymic());
        }
        return dto;
    }

    public AddWorkerUpdateResponse update(AddWorkerUpdateRequest addWorkerUpdateRequest) {
        BaseUser baseUser = baseUserRepository.findById(addWorkerUpdateRequest.getId())
                .orElseThrow(() -> new UserNotFountException("No such user found."));
//        baseUser.getAuthorities().stream()
//                .map(roleFactories::get)
//                .filter(Objects::nonNull)
//                .forEach(factory -> factory.updateUser(addWorkerUpdateRequest));
        return addWorkerUpdateResponse(addWorkerUpdateRequest);
    }


    private AddWorkerUpdateResponse addWorkerUpdateResponse(AddWorkerUpdateRequest addWorkerUpdateRequest) {
        return AddWorkerUpdateResponse.builder()
                .username(addWorkerUpdateRequest.getUsername())
                .password(addWorkerUpdateRequest.getPassword())
                .name(addWorkerUpdateRequest.getName())
                .surname(addWorkerUpdateRequest.getSurname())
                .patronymic(addWorkerUpdateRequest.getPatronymic())
                .colorCode(addWorkerUpdateRequest.getColorCode())
                .email(addWorkerUpdateRequest.getEmail())
                .finCode(addWorkerUpdateRequest.getFinCode())
                .dateOfBirth(addWorkerUpdateRequest.getDateOfBirth())
                .phone(addWorkerUpdateRequest.getPhone())
                .genderStatus(addWorkerUpdateRequest.getGenderStatus())
                .address(addWorkerUpdateRequest.getAddress())
                .experience(addWorkerUpdateRequest.getExperience())
                .degree(addWorkerUpdateRequest.getDegree())
                .phone2(addWorkerUpdateRequest.getPhone2())
                .homePhone(addWorkerUpdateRequest.getHomePhone())
                .authorities(addWorkerUpdateRequest.getAuthorities())
                .enabled(true)
                .build();
    }

    public String delete(UUID id) {
        BaseUser baseUser = baseUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFountException("No such user found."));
        baseUser.getAuthorities().stream()
                .map(roleFactories::get)
                .filter(Objects::nonNull)
                .forEach(factory -> factory.deleteUser(id));
        return "Silindi";
    }

    public AddWorkerReadResponse info(UUID id) {
        BaseUser baseUser = baseUserRepository.findById(id)
                .orElseThrow(() -> new UserNotFountException("No such user found."));

        String role = baseUser.getUserType();

        return switch (role) {
            case "ADMIN" -> convertToDto(adminService.findById(id));
            case "DOCTOR" -> convertToDto(doctorService.findById(id));
            case "RECEPTION" -> convertToDto(receptionService.findById(id));
            default -> throw new RuntimeException("Unknown role: " + role);
        };
    }

    public List<AddWorkerReadResponse> search(AddWorkerSearchRequest addWorkerSearchRequest) {
        List<BaseUser> users = baseUserRepository.findAll(UserSpecification.filterByWorker(addWorkerSearchRequest));
        return addWorkerMapper.toResponses(users);
    }

    public List<AddWorkerReadStatusResponse> readStatus() {
        return Arrays.stream(Role.values())
                .map(role -> new AddWorkerReadStatusResponse(role.getAuthority())) 
                .collect(Collectors.toList());
    }
}
