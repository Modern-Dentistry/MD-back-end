package com.rustam.modern_dentistry.dao.repository.settings.recipes;

import com.rustam.modern_dentistry.dao.entity.settings.recipes.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    boolean existsByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = {"medicines"})
    List<Recipe> findAll();

    @EntityGraph(attributePaths = {"medicines"})
    Optional<Recipe> findById(Long id);

    @EntityGraph(attributePaths = {"medicines"})
    Page<Recipe> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"medicines"})
    Page<Recipe> findAll(Specification<Recipe> spec, Pageable pageable);
}
