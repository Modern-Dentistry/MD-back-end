package com.rustam.modern_dentistry.util.specification;

import com.rustam.modern_dentistry.dao.entity.OperationType;
import com.rustam.modern_dentistry.dto.request.read.OperationTypeSearchRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OperationTypeSpecification {

    public static Specification<OperationType> filterBy(OperationTypeSearchRequest request) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }
            if (request.getCategoryName() != null && !request.getCategoryName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("categoryName")), "%" + request.getCategoryName().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
