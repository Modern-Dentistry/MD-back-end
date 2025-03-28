package com.rustam.modern_dentistry.util.specification.settings;

import com.rustam.modern_dentistry.dao.entity.settings.operations.OpTypeItem;
import org.springframework.data.jpa.domain.Specification;

public class OpTypeItemReadSpec {
    public static Specification<OpTypeItem> hasId(Long id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), id);
    }
}
