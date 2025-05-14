package com.rustam.modern_dentistry.controller.laboratory;

import com.rustam.modern_dentistry.dto.request.DentalOrderCreateReq;
import com.rustam.modern_dentistry.dto.request.create.TechnicianCreateRequest;
import com.rustam.modern_dentistry.service.labarotory.DentalOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/laboratory")
@RequiredArgsConstructor
public class DentalOrderController {
    private final DentalOrderService dentalOrderService;

    @PostMapping("/order/create")
    public ResponseEntity<Void> create(@Valid @RequestBody DentalOrderCreateReq request) {
        dentalOrderService.create(request);
        return ResponseEntity.ok().build();
    }
}
