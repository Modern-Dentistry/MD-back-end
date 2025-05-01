package com.rustam.modern_dentistry.dto.response.read;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatRecipeReadRes {
    Long recipeId;
    Long patientId;
    String recipeName;
    LocalDate date;
}
