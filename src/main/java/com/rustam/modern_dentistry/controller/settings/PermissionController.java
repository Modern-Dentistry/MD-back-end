package com.rustam.modern_dentistry.controller.settings;

import com.rustam.modern_dentistry.dto.request.create.PermissionCreateRequest;
import com.rustam.modern_dentistry.dto.response.read.PermissionResponse;
import com.rustam.modern_dentistry.service.settings.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping(path = "/create")
    public ResponseEntity<PermissionResponse> create(@RequestBody PermissionCreateRequest permissionCreateRequest){
        return new ResponseEntity<>(permissionService.create(permissionCreateRequest), HttpStatus.CREATED);
    }


}
