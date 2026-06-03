package com.formularac.formula.service;

import com.formularac.formula.api.dto.FormulaIngredientRequest;
import com.formularac.formula.api.dto.FormulaItemResponse;
import com.formularac.formula.api.dto.FormulaOptimizationRequest;
import com.formularac.formula.api.dto.FormulaOptimizationResponse;
import com.formularac.formula.api.dto.NutrientConstraintRequest;
import com.formularac.formula.api.dto.NutrientResultResponse;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FormulaOptimizationService {

    private static final int SCALE = 6;

    public FormulaOptimizationResponse optimize(FormulaOptimizationRequest request) {
        validatePercentRanges(request.ingredients());

        ExpressionsBasedModel model = new ExpressionsBasedModel();
        Map<FormulaIngredientRequest, Variable> variables = new LinkedHashMap<>();

        for (FormulaIngredientRequest ingredient : request.ingredients()) {
            Variable variable = model.addVariable(ingredient.name())
                    .lower(toFraction(ingredient.minPercent()))
                    .upper(toFraction(ingredient.maxPercent()))
                    .weight(ingredient.pricePerKg());
            variables.put(ingredient, variable);
        }

        Expression totalInclusion = model.addExpression("totalInclusion").level(BigDecimal.ONE);
        variables.values().forEach(variable -> totalInclusion.set(variable, BigDecimal.ONE));

        for (NutrientConstraintRequest constraint : request.nutrientConstraints()) {
            Expression expression = model.addExpression("nutrient:" + constraint.name());
            if (constraint.minValue() != null) {
                expression.lower(constraint.minValue());
            }
            if (constraint.maxValue() != null) {
                expression.upper(constraint.maxValue());
            }

            for (Map.Entry<FormulaIngredientRequest, Variable> entry : variables.entrySet()) {
                BigDecimal nutrientValue = entry.getKey().nutrients().get(constraint.name());
                if (nutrientValue == null) {
                    throw new FormulaOptimizationException("Ingrediente '%s' nao possui valor para o nutriente '%s'."
                            .formatted(entry.getKey().name(), constraint.name()));
                }
                expression.set(entry.getValue(), nutrientValue);
            }
        }

        Optimisation.Result result = model.minimise();
        if (!result.getState().isFeasible()) {
            throw new FormulaOptimizationException("Nao foi possivel encontrar uma formula viavel com as restricoes informadas.");
        }

        return buildResponse(request, variables, result);
    }

    private FormulaOptimizationResponse buildResponse(
            FormulaOptimizationRequest request,
            Map<FormulaIngredientRequest, Variable> variables,
            Optimisation.Result result
    ) {
        BigDecimal batchSizeKg = request.batchSizeKg();
        List<FormulaItemResponse> items = new ArrayList<>();
        List<NutrientResultResponse> nutrients = new ArrayList<>();

        BigDecimal totalCost = BigDecimal.ZERO;
        for (Map.Entry<FormulaIngredientRequest, Variable> entry : variables.entrySet()) {
            FormulaIngredientRequest ingredient = entry.getKey();
            BigDecimal inclusionFraction = BigDecimal.valueOf(entry.getValue().getValue().doubleValue());
            BigDecimal inclusionPercent = inclusionFraction.multiply(BigDecimal.valueOf(100)).setScale(SCALE, RoundingMode.HALF_UP);
            BigDecimal quantityKg = inclusionFraction.multiply(batchSizeKg).setScale(SCALE, RoundingMode.HALF_UP);
            BigDecimal cost = quantityKg.multiply(ingredient.pricePerKg()).setScale(SCALE, RoundingMode.HALF_UP);
            totalCost = totalCost.add(cost);

            items.add(new FormulaItemResponse(ingredient.name(), inclusionPercent, quantityKg, cost));
        }

        for (NutrientConstraintRequest constraint : request.nutrientConstraints()) {
            BigDecimal value = BigDecimal.ZERO;
            for (Map.Entry<FormulaIngredientRequest, Variable> entry : variables.entrySet()) {
                BigDecimal inclusionFraction = BigDecimal.valueOf(entry.getValue().getValue().doubleValue());
                value = value.add(inclusionFraction.multiply(entry.getKey().nutrients().get(constraint.name())));
            }
            nutrients.add(new NutrientResultResponse(
                    constraint.name(),
                    value.setScale(SCALE, RoundingMode.HALF_UP),
                    constraint.minValue(),
                    constraint.maxValue()
            ));
        }

        BigDecimal costPerKg = totalCost.divide(batchSizeKg, SCALE, RoundingMode.HALF_UP);
        BigDecimal costPerTon = costPerKg.multiply(BigDecimal.valueOf(1000)).setScale(SCALE, RoundingMode.HALF_UP);

        return new FormulaOptimizationResponse(
                result.getState().toString(),
                totalCost.setScale(SCALE, RoundingMode.HALF_UP),
                costPerKg,
                costPerTon,
                items,
                nutrients
        );
    }

    private void validatePercentRanges(List<FormulaIngredientRequest> ingredients) {
        for (FormulaIngredientRequest ingredient : ingredients) {
            if (ingredient.maxPercent().compareTo(ingredient.minPercent()) < 0) {
                throw new FormulaOptimizationException("Ingrediente '%s' tem maxPercent menor que minPercent.".formatted(ingredient.name()));
            }
        }
    }

    private BigDecimal toFraction(BigDecimal percent) {
        return percent.divide(BigDecimal.valueOf(100), SCALE, RoundingMode.HALF_UP);
    }
}
