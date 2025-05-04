package com.rustam.modern_dentistry.controller.warehouse_operations;

import com.rustam.modern_dentistry.dao.entity.warehouse_operations.WarehouseRemoval;
import com.rustam.modern_dentistry.dto.request.create.WarehouseRemovalCreateRequest;
import com.rustam.modern_dentistry.dto.response.create.WarehouseRemovalCreateResponse;
import com.rustam.modern_dentistry.service.warehouse_operations.WarehouseRemovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/warehouse-removal")
@RequiredArgsConstructor
public class WarehouseRemovalController {

    private final WarehouseRemovalService warehouseRemovalService;

}
