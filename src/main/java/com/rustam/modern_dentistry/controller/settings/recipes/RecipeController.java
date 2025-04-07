package com.rustam.modern_dentistry.controller.settings.recipes;

import com.rustam.modern_dentistry.dto.request.create.RecipeCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.RecipeUpdateRequest;
import com.rustam.modern_dentistry.dto.response.RecipeReadResponse;
import com.rustam.modern_dentistry.service.settings.recipes.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(path = "/api/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody RecipeCreateRequest request) {
        recipeService.create(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/read")
    public ResponseEntity<List<RecipeReadResponse>> read() {
        return ResponseEntity.ok(recipeService.read());
    }

    @GetMapping("/read-by-id")
    public ResponseEntity<RecipeReadResponse> readById(@RequestParam Long id) {
        return ResponseEntity.ok(recipeService.readById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @RequestBody RecipeUpdateRequest request) {
        recipeService.update(id, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update-status/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id) {
        recipeService.updateStatus(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recipeService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
