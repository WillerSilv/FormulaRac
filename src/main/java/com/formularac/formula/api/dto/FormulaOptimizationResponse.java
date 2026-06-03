package com.formularac.formula.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record FormulaOptimizationResponse(
        String status,
        BigDecimal totalCost,
        BigDecimal costPerKg,
        BigDecimal costPerTon,
        List<FormulaItemResponse> items,
        List<NutrientResultResponse> nutrients
) {
}
