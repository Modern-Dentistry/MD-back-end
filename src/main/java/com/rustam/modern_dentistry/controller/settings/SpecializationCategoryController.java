package com.rustam.modern_dentistry.controller.settings;


import com.rustam.modern_dentistry.dto.request.create.SpecializationCategoryCreateRequest;
import com.rustam.modern_dentistry.dto.request.read.SpecializationCategoryRequest;
import com.rustam.modern_dentistry.dto.request.update.SpecializationCategoryUpdateRequest;
import com.rustam.modern_dentistry.dto.response.read.SpecializationCategoryResponse;
import com.rustam.modern_dentistry.service.settings.SpecializationCategoryService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(path = "/api/v1/specialization")
public class SpecializationCategoryController {

    SpecializationCategoryService specializationCategoryService;

    @PostMapping(path = "/create")
    public ResponseEntity<SpecializationCategoryResponse> createSpecializationCategory(@RequestBody SpecializationCategoryCreateRequest specializationCategoryCreateRequest){
        return new ResponseEntity<>(specializationCategoryService.create(specializationCategoryCreateRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/read")
    public ResponseEntity<List<SpecializationCategoryResponse>> readSpecializationCategory(){
        return new ResponseEntity<>(specializationCategoryService.read(),HttpStatus.OK);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<SpecializationCategoryResponse>  updateSpecializationCategory(@RequestBody SpecializationCategoryUpdateRequest specializationCategoryUpdateRequest){
        return new ResponseEntity<>(specializationCategoryService.update(specializationCategoryUpdateRequest),HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<Void> deleteSpecializationCategory(@PathVariable Long id){
        specializationCategoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
