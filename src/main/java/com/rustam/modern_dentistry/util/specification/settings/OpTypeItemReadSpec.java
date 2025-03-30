package com.rustam.modern_dentistry.util.specification.settings;

import com.rustam.modern_dentistry.dao.entity.settings.operations.OpType;
import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class OpTypeItemReadSpec {
    public static Specification<OpTypeItem> hasId(Long id) {
        return (root, query, criteriaBuilder) -> {
            Join<OpTypeItem, OpType> opTypeJoin = root.join("opType", JoinType.INNER);

            return criteriaBuilder.equal(opTypeJoin.get("id"), id);
        };
    }
}
