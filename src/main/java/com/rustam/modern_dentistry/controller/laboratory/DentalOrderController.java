package com.rustam.modern_dentistry.controller.laboratory;

import com.rustam.modern_dentistry.dao.entity.enums.DentalWorkType;
import com.rustam.modern_dentistry.dto.request.DentalOrderCreateReq;
import com.rustam.modern_dentistry.dto.request.update.UpdateLabOrderStatus;
import com.rustam.modern_dentistry.dto.request.update.UpdateOrderPrice;
import com.rustam.modern_dentistry.dto.request.update.UpdateTechnicianOrderReq;
import com.rustam.modern_dentistry.dto.response.read.TechnicianOrderResponse;
import com.rustam.modern_dentistry.service.labarotory.DentalOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(path = "/api/v1/laboratory")
@RequiredArgsConstructor
public class DentalOrderController {
    private final DentalOrderService dentalOrderService;

    @PostMapping(path = "/order/create")
    public ResponseEntity<Void> create(@RequestBody DentalOrderCreateReq request) {
        dentalOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/order/read")
    public ResponseEntity<List<TechnicianOrderResponse>> read() {
        return ResponseEntity.ok(dentalOrderService.read());
    }

    @GetMapping(path = "/order/read/dental-work-type")
    public ResponseEntity<List<DentalWorkType>> readByDentalWorkType() {
        return new ResponseEntity<>(dentalOrderService.readByDentalWorkType(),HttpStatus.OK);
    }

    @GetMapping("/order/read-by-id/{id}")
    public ResponseEntity<TechnicianOrderResponse> readById(@PathVariable Long id) {
        return ResponseEntity.ok(dentalOrderService.readById(id));
    }

    @PutMapping(path = "/order/update")
    public ResponseEntity<Void> update(@RequestBody @Valid UpdateTechnicianOrderReq request) {
        dentalOrderService.update(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(path = "/order/status")
    public ResponseEntity<Void> updateOrderStatus(UpdateLabOrderStatus request) {
        dentalOrderService.updateOrderStatus(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/order/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dentalOrderService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    // Technic apis
    // After user login get technic's orders
    // @PreAuthorize("hasRole('TECHNICIAN')")
    @GetMapping("/technic/order/read")
    public ResponseEntity<List<TechnicianOrderResponse>> getTechnicianOrdersByUUID() {
        return ResponseEntity.ok(dentalOrderService.getTechnicianOrdersByUUID());
    }

    @PatchMapping(path = "/technic/order/{id}/price")
    public ResponseEntity<Void> updateOrderStatus(@PathVariable Long id,
                                                  @Valid @RequestBody UpdateOrderPrice request) {
        dentalOrderService.setOrderPrice(id, request);
        return ResponseEntity.ok().build();
    }

}
