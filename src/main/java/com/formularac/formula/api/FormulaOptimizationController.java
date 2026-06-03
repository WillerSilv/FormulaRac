package com.formularac.formula.api;

import com.formularac.formula.api.dto.FormulaOptimizationRequest;
import com.formularac.formula.api.dto.FormulaOptimizationResponse;
import com.formularac.formula.service.FormulaOptimizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/formulas")
@RequiredArgsConstructor
public class FormulaOptimizationController {

    private final FormulaOptimizationService optimizationService;

    @PostMapping("/optimize")
    public FormulaOptimizationResponse optimize(@Valid @RequestBody FormulaOptimizationRequest request) {
        return optimizationService.optimize(request);
    }
}
