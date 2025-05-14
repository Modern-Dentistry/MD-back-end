package com.rustam.modern_dentistry.controller.laboratory;

import com.rustam.modern_dentistry.dto.request.DentalOrderCreateReq;
import com.rustam.modern_dentistry.dto.request.create.PatPhotosCreateReq;
import com.rustam.modern_dentistry.dto.request.create.TechnicianCreateRequest;
import com.rustam.modern_dentistry.service.labarotory.DentalOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path = "/api/v1/laboratory")
@RequiredArgsConstructor
public class DentalOrderController {
    private final DentalOrderService dentalOrderService;

    @PostMapping(path = "/order/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> create(@RequestPart("data") @Valid DentalOrderCreateReq request,
                                       @RequestPart("files") List<MultipartFile> files) {
        dentalOrderService.create(request, files);
        return ResponseEntity.status(CREATED).build();
    }
}
