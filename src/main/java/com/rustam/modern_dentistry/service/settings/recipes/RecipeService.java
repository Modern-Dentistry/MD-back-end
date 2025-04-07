package com.rustam.modern_dentistry.service.settings.recipes;

import com.rustam.modern_dentistry.dao.entity.settings.recipes.Recipe;
import com.rustam.modern_dentistry.dao.repository.settings.recipes.RecipeRepository;
import com.rustam.modern_dentistry.dto.request.create.RecipeCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.RecipeUpdateRequest;
import com.rustam.modern_dentistry.dto.response.RecipeReadResponse;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.settings.recipes.RecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final RecipeRepository recipeRepository;

    public void create(RecipeCreateRequest request) {

    }

    public List<RecipeReadResponse> read() {
        return null;
    }

    public void update(Long id, RecipeUpdateRequest request) {

    }

    public void updateStatus(Long id) {
    }

    public void delete(Long id) {
    }

    private Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də resept tapımadı: " + id));

    }
}
