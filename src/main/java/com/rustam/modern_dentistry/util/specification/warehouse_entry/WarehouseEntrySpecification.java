package com.rustam.modern_dentistry.util.specification.warehouse_entry;

import com.rustam.modern_dentistry.dao.entity.warehouse_entry.WarehouseEntry;
import com.rustam.modern_dentistry.dto.request.read.WarehouseEntrySearchRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public class WarehouseEntrySpecification {

    public static Specification<WarehouseEntry> filterBy(WarehouseEntrySearchRequest request) {
        return (root, query, cb) -> {
            Specification<WarehouseEntry> spec = Specification.where(null);

            if (request.getDate() != null) {
                spec = spec.and((r, q, c) -> c.equal(r.get("date"), request.getDate()));
            }

            if (request.getTime() != null) {
                spec = spec.and((r, q, c) -> c.equal(r.get("time"), request.getTime()));
            }

            return spec.toPredicate(root, query, cb);
        };
    }
}

