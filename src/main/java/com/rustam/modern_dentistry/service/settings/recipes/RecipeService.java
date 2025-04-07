package com.rustam.modern_dentistry.service.settings.recipes;

import com.rustam.modern_dentistry.dao.repository.settings.recipes.RecipeRepository;
import com.rustam.modern_dentistry.mapper.settings.recipes.RecipeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeMapper recipeMapper;
    private final RecipeRepository recipeRepository;
}
