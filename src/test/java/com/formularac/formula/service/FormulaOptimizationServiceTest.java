package com.formularac.formula.service;

import com.formularac.formula.api.dto.FormulaIngredientRequest;
import com.formularac.formula.api.dto.FormulaItemResponse;
import com.formularac.formula.api.dto.FormulaOptimizationRequest;
import com.formularac.formula.api.dto.FormulaOptimizationResponse;
import com.formularac.formula.api.dto.NutrientConstraintRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FormulaOptimizationServiceTest {

    private final FormulaOptimizationService service = new FormulaOptimizationService();

    @Test
    void shouldOptimizeLeastCostFormulaRespectingNutrientConstraints() {
        FormulaOptimizationRequest request = new FormulaOptimizationRequest(
                BigDecimal.valueOf(1000),
                List.of(
                        new FormulaIngredientRequest(
                                "Milho",
                                BigDecimal.valueOf(1.20),
                                BigDecimal.ZERO,
                                BigDecimal.valueOf(100),
                                Map.of(
                                        "Proteina Bruta", BigDecimal.valueOf(8),
                                        "Energia Metabolizavel", BigDecimal.valueOf(3300)
                                )
                        ),
                        new FormulaIngredientRequest(
                                "Farelo de Soja",
                                BigDecimal.valueOf(2.50),
                                BigDecimal.ZERO,
                                BigDecimal.valueOf(100),
                                Map.of(
                                        "Proteina Bruta", BigDecimal.valueOf(45),
                                        "Energia Metabolizavel", BigDecimal.valueOf(2400)
                                )
                        )
                ),
                List.of(
                        new NutrientConstraintRequest("Proteina Bruta", BigDecimal.valueOf(18), null),
                        new NutrientConstraintRequest("Energia Metabolizavel", BigDecimal.valueOf(2800), null)
                )
        );

        FormulaOptimizationResponse response = service.optimize(request);
        BigDecimal totalInclusionPercent = response.items().stream()
                .map(FormulaItemResponse::inclusionPercent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(response.status()).isIn("OPTIMAL", "FEASIBLE");
        assertThat(totalInclusionPercent).isCloseTo(BigDecimal.valueOf(100), withinTolerance());
        assertThat(response.nutrients())
                .anySatisfy(nutrient -> {
                    assertThat(nutrient.name()).isEqualTo("Proteina Bruta");
                    assertThat(nutrient.value()).isGreaterThanOrEqualTo(BigDecimal.valueOf(18));
                })
                .anySatisfy(nutrient -> {
                    assertThat(nutrient.name()).isEqualTo("Energia Metabolizavel");
                    assertThat(nutrient.value()).isGreaterThanOrEqualTo(BigDecimal.valueOf(2800));
                });
        assertThat(response.costPerTon()).isPositive();
    }

    private org.assertj.core.data.Offset<BigDecimal> withinTolerance() {
        return org.assertj.core.data.Offset.offset(BigDecimal.valueOf(0.0001));
    }
}
