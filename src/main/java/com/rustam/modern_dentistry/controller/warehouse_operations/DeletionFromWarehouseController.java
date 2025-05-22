package com.rustam.modern_dentistry.controller.warehouse_operations;

import com.rustam.modern_dentistry.dto.request.create.DeletionFromWarehouseCreateRequest;
import com.rustam.modern_dentistry.dto.response.read.DeletionFromWarehouseResponse;
import com.rustam.modern_dentistry.service.warehouse_operations.DeletionFromWarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/deletion-from-warehouse")
@RequiredArgsConstructor
public class DeletionFromWarehouseController {

    private final DeletionFromWarehouseService deletionFromWarehouseService;

    @PostMapping(path = "/create")
    public ResponseEntity<DeletionFromWarehouseResponse> create(@RequestBody DeletionFromWarehouseCreateRequest deletionFromWarehouseCreateRequest){
        return new ResponseEntity<>(deletionFromWarehouseService.create(deletionFromWarehouseCreateRequest), HttpStatus.CREATED);
    }
}
