package com.rustam.modern_dentistry.mapper.settings.recipes;

import com.rustam.modern_dentistry.dao.entity.settings.recipes.Recipe;
import com.rustam.modern_dentistry.dto.request.create.RecipeCreateRequest;
import com.rustam.modern_dentistry.dto.request.update.RecipeUpdateRequest;
import com.rustam.modern_dentistry.dto.response.RecipeReadResponse;
import org.mapstruct.*;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RecipeMapper {

    Recipe toEntity(RecipeCreateRequest recipe);

    @Mapping(target = "medicineCount", expression = "java(recipe.getMedicines().size())")
    RecipeReadResponse toResponse(Recipe recipe);

    void update(@MappingTarget Recipe recipe, RecipeUpdateRequest request);
}
