package com.formularac.formula.api.dto;

import java.math.BigDecimal;

public record FormulaItemResponse(
        String ingredientName,
        BigDecimal inclusionPercent,
        BigDecimal quantityKg,
        BigDecimal cost
) {
}
