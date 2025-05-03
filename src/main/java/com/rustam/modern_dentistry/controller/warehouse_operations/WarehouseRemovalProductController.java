package com.rustam.modern_dentistry.controller.warehouse_operations;

import com.rustam.modern_dentistry.dto.request.create.WarehouseRemovalCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.WarehouseRemovalProductSearchRequest;
import com.rustam.modern_dentistry.dto.response.create.WarehouseRemovalCreateResponse;
import com.rustam.modern_dentistry.dto.response.read.WarehouseRemovalProductReadResponse;
import com.rustam.modern_dentistry.service.warehouse_operations.WarehouseRemovalProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/warehouse-removal-product")
@RequiredArgsConstructor
public class WarehouseRemovalProductController {

    private final WarehouseRemovalProductService warehouseRemovalProductService;

    @PostMapping(path = "/create")
    public ResponseEntity<WarehouseRemovalCreateResponse> create(@RequestBody WarehouseRemovalCreateRequest warehouseRemovalCreateRequest){
        return new ResponseEntity<>(warehouseRemovalProductService.create(warehouseRemovalCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/read")
    public ResponseEntity<List<WarehouseRemovalProductReadResponse>> read(){
        return new ResponseEntity<>(warehouseRemovalProductService.read(),HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<WarehouseRemovalProductReadResponse>> search(@RequestBody WarehouseRemovalProductSearchRequest warehouseRemovalProductSearchRequest) {
        return new ResponseEntity<>(warehouseRemovalProductService.search(warehouseRemovalProductSearchRequest),HttpStatus.OK);
    }

    @GetMapping(path = "/info/{id}")
    public ResponseEntity<WarehouseRemovalCreateResponse> getWarehouseRemovalProductInfo(@PathVariable Long id){
        return new ResponseEntity<>(warehouseRemovalProductService.info(id),HttpStatus.OK);
    }

    @DeleteMapping(path = "/warehouse-removal-id/{id}/to-warehouse-removal-product-id/{warehouseRemovalProductId}")
    public ResponseEntity<Void> deleteWarehouseRemovalIdBasedOnWarehouseRemovalProduct(@PathVariable Long id,@PathVariable Long warehouseRemovalProductId){
        warehouseRemovalProductService.deleteWarehouseRemovalIdBasedOnWarehouseRemovalProduct(id,warehouseRemovalProductId);
        return ResponseEntity.ok().build();
    }
}
