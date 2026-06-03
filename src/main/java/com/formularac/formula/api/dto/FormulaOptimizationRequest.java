package com.formularac.formula.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record FormulaOptimizationRequest(
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal batchSizeKg,
        @NotEmpty List<@Valid FormulaIngredientRequest> ingredients,
        @NotEmpty List<@Valid NutrientConstraintRequest> nutrientConstraints
) {
}
