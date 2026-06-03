package com.formularac.formula.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Map;

public record FormulaIngredientRequest(
        @NotBlank String name,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal pricePerKg,
        @NotNull @DecimalMin("0.0") BigDecimal minPercent,
        @NotNull @DecimalMin("0.0") BigDecimal maxPercent,
        @NotEmpty Map<String, BigDecimal> nutrients
) {
}
