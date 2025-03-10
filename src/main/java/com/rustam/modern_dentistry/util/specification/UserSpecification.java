package com.rustam.modern_dentistry.util.specification;

import com.rustam.modern_dentistry.dao.entity.users.BaseUser;
import com.rustam.modern_dentistry.dao.entity.users.Patient;
import com.rustam.modern_dentistry.dto.request.read.AddWorkerSearchRequest;
import com.rustam.modern_dentistry.dto.request.read.PatientSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<Patient> filterBy(PatientSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null && !request.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
            }
            if (request.getSurname() != null && !request.getSurname().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + request.getSurname().toLowerCase() + "%"));
            }
            if (request.getFinCode() != null && !request.getFinCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("finCode")), "%" + request.getFinCode().toLowerCase() + "%"));
            }
            if (request.getPhone() != null && !request.getPhone().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + request.getPhone().toLowerCase() + "%"));
            }
            if (request.getGenderStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("genderStatus"), request.getGenderStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<BaseUser> filterByWorker(AddWorkerSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getUsername() != null && !request.getUsername().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + request.getUsername().toLowerCase() + "%"));
            }
            if (request.getName() != null && !request.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
            }
            if (request.getSurname() != null && !request.getSurname().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + request.getSurname().toLowerCase() + "%"));
            }
            if (request.getPatronymic() != null && !request.getPatronymic().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("patronymic")), "%" + request.getPatronymic().toLowerCase() + "%"));
            }
            if (request.getPhone() != null && !request.getPhone().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + request.getPhone().toLowerCase() + "%"));
            }
            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
