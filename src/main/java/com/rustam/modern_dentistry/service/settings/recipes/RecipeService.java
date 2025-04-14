package com.rustam.modern_dentistry.service.settings.recipes;

import com.rustam.modern_dentistry.dao.entity.enums.status.Status;
import com.rustam.modern_dentistry.dao.entity.settings.recipes.Recipe;
import com.rustam.modern_dentistry.dao.repository.settings.recipes.RecipeRepository;
import com.rustam.modern_dentistry.dto.request.create.RecipeCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.RecipeUpdateRequest;
import com.rustam.modern_dentistry.dto.response.RecipeReadResponse;
import com.rustam.modern_dentistry.exception.custom.ExistsException;
import com.rustam.modern_dentistry.exception.custom.NotFoundException;
import com.rustam.modern_dentistry.mapper.settings.recipes.RecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rustam.modern_dentistry.dao.entity.enums.status.Status.ACTIVE;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final RecipeRepository recipeRepository;

    public void create(RecipeCreateRequest request) {
        checkNameIfAlreadyExist(request.getName());
        Recipe recipe = recipeMapper.toEntity(request);
        recipeRepository.save(recipe);
    }

    public List<RecipeReadResponse> read() {
        var recipes = recipeRepository.findAll();
        return recipes.stream().map(recipeMapper::toResponse).toList();
    }

    public RecipeReadResponse readById(Long id) {
        var recipe = getRecipeById(id);
        return recipeMapper.toResponse(recipe);
    }

    public void update(Long id, RecipeUpdateRequest request) {
        var recipe = getRecipeById(id);
        recipeMapper.update(recipe, request);
        recipeRepository.save(recipe);
    }

    public void updateStatus(Long id) {
        var recipe = getRecipeById(id);
        recipe.setStatus(recipe.getStatus() == ACTIVE ? Status.PASSIVE : ACTIVE);
        recipeRepository.save(recipe);
    }

    public void delete(Long id) {
        var recipe = getRecipeById(id);
        recipeRepository.delete(recipe);
    }

    private void checkNameIfAlreadyExist(String name) {
        var result = recipeRepository.existsByNameIgnoreCase(name);
        if (result) throw new ExistsException("Bu ad artıq əlavə edilib.");
    }

    protected Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Bu ID-də resept tapımadı: " + id));
    }
}
