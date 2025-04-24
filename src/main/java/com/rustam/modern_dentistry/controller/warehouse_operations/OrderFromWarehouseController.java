package com.rustam.modern_dentistry.controller.warehouse_operations;

import com.rustam.modern_dentistry.dto.request.create.OrderFromWarehouseCreateRequest;
import com.rustam.modern_dentistry.dto.response.read.OrderFromWarehouseResponse;
import com.rustam.modern_dentistry.service.warehouse_operations.OrderFromWarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/order-from-warehouse")
@RequiredArgsConstructor
public class OrderFromWarehouseController {

    private final OrderFromWarehouseService orderFromWarehouseService;

    @PostMapping(path = "/create")
    public ResponseEntity<OrderFromWarehouseResponse> create(@Valid @RequestBody OrderFromWarehouseCreateRequest orderFromWarehouseCreateRequest){
        return new ResponseEntity<>(orderFromWarehouseService.create(orderFromWarehouseCreateRequest), HttpStatus.CREATED);
    }

}
