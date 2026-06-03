package com.formularac.formula.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record NutrientConstraintRequest(
        @NotBlank String name,
        BigDecimal minValue,
        BigDecimal maxValue
) {
}
