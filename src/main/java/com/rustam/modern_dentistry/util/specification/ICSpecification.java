package com.rustam.modern_dentistry.util.specification;

import com.rustam.modern_dentistry.dao.entity.InsuranceCompany;
import com.rustam.modern_dentistry.dao.entity.OperationType;
import com.rustam.modern_dentistry.dto.request.read.ICSearchRequest;
import com.rustam.modern_dentistry.dto.request.read.OperationTypeSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ICSpecification {
    public static Specification<InsuranceCompany> filterBy(ICSearchRequest request) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }
            if (request.getCompanyName() != null && !request.getCompanyName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("categoryName")), "%" + request.getCompanyName().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
