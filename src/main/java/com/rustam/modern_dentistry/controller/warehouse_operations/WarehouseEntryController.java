package com.rustam.modern_dentistry.controller.warehouse_operations;


import com.rustam.modern_dentistry.dto.request.create.WarehouseEntryCreateRequest;
import com.rustam.modern_dentistry.dto.response.create.ProductCategoryResponse;
import com.rustam.modern_dentistry.dto.response.create.WarehouseEntryCreateResponse;
import com.rustam.modern_dentistry.service.warehouse_entry.WarehouseEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/warehouse-entry")
@RequiredArgsConstructor
public class WarehouseEntryController {

    private final WarehouseEntryService warehouseEntryService;

    @PostMapping(path = "/create")
    public ResponseEntity<WarehouseEntryCreateResponse> create(@RequestBody WarehouseEntryCreateRequest warehouseEntryCreateRequest){
        return new ResponseEntity<>(warehouseEntryService.create(warehouseEntryCreateRequest),HttpStatus.CREATED);
    }

}
