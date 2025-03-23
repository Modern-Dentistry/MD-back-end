package com.rustam.modern_dentistry.controller.teeth;

import com.rustam.modern_dentistry.dto.request.create.CreateTeethOperationRequest;
import com.rustam.modern_dentistry.dto.response.read.TeethOperationResponse;
import com.rustam.modern_dentistry.service.TeethOperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/teeth-operation")
@RequiredArgsConstructor
public class TeethOperationController {

    private final TeethOperationService teethOperationService;

    @PostMapping(path = "/create")
    public ResponseEntity<Void> create(@Valid @RequestBody CreateTeethOperationRequest createTeethOperationRequest){
        teethOperationService.create(createTeethOperationRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/read")
    public ResponseEntity<List<TeethOperationResponse>> read(){
        return new ResponseEntity<>(teethOperationService.read(), HttpStatus.OK);
    }
}
