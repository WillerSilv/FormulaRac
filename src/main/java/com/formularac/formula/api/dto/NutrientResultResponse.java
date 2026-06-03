package com.formularac.formula.api.dto;

import java.math.BigDecimal;

public record NutrientResultResponse(
        String name,
        BigDecimal value,
        BigDecimal minValue,
        BigDecimal maxValue
) {
}
