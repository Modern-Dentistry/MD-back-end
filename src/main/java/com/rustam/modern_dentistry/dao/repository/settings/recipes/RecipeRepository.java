package com.rustam.modern_dentistry.dao.repository.settings.recipes;

import com.rustam.modern_dentistry.dao.entity.settings.recipes.Recipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    boolean existsByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = {"medicines"})
    List<Recipe> findAll();
}
