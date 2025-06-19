package com.rustam.modern_dentistry.controller.settings;

import com.rustam.modern_dentistry.dto.request.create.PermissionCreateRequest;
import com.rustam.modern_dentistry.dto.response.read.InfoPermissionResponse;
import com.rustam.modern_dentistry.dto.response.read.PermissionResponse;
import com.rustam.modern_dentistry.service.settings.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping(path = "/create")
    public ResponseEntity<PermissionResponse> create(@RequestBody PermissionCreateRequest permissionCreateRequest){
        return new ResponseEntity<>(permissionService.create(permissionCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/read")
    public ResponseEntity<List<PermissionResponse>> read(){
        return new ResponseEntity<>(permissionService.read(),HttpStatus.OK);
    }

    @GetMapping(path = "/info/{id}")
    public ResponseEntity<InfoPermissionResponse> info(@PathVariable Long id){
        return new ResponseEntity<>(permissionService.info(id),HttpStatus.OK);
    }

}
