package com.rustam.modern_dentistry.dao.repository.settings.recipes;

import com.rustam.modern_dentistry.dao.entity.settings.recipes.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
