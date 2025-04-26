package com.rustam.modern_dentistry.dto.request.update;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.rustam.modern_dentistry.dao.entity.enums.ValidationErrorMessage.*;

@Getter
@Setter
public class PatRecipeUpdateReq {
    @NotNull(message = VALIDATION_RECIPE_REQUIRED)
    Long recipeId;
    @NotNull(message = VALIDATION_DATE_REQUIRED)
    LocalDate date;
}
