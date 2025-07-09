package com.rustam.modern_dentistry.controller.settings.implant;

import com.rustam.modern_dentistry.dto.request.create.ImplantSizeCreateRequest;
import com.rustam.modern_dentistry.dto.response.read.ImplantSizeResponse;
import com.rustam.modern_dentistry.service.settings.implant.ImplantSizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/implant-size")
@RequiredArgsConstructor
public class ImplantSizeController {

    private final ImplantSizeService implantSizeService;

    @PostMapping(path = "/create")
    public ResponseEntity<ImplantSizeResponse> create(@RequestBody ImplantSizeCreateRequest implantSizeCreateRequest){
        return new ResponseEntity<>(implantSizeService.create(implantSizeCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/read")
    public ResponseEntity<List<ImplantSizeResponse>> read(){
        return new ResponseEntity<>(implantSizeService.read(),HttpStatus.OK);
    }

}
