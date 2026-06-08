package com.formularac.servico;

import com.formularac.dominio.ExigenciaNutricional;
import com.formularac.dominio.Formula;
import com.formularac.dominio.ResultadoAvaliacaoNutriente;
import com.formularac.dominio.ValorNutricionalIngrediente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AvaliadorFormula {

    private final CalculadoraFormula calculadoraFormula;

    public AvaliadorFormula() {
        this.calculadoraFormula = new CalculadoraFormula();
    }

    public AvaliadorFormula(CalculadoraFormula calculadoraFormula) {
        if (calculadoraFormula == null) {
            throw new IllegalArgumentException("Calculadora da formula não pode ser nula");
        }

        this.calculadoraFormula = calculadoraFormula;
    }

    public List<ResultadoAvaliacaoNutriente> avaliar(Formula formula,
                                                     List<ExigenciaNutricional> exigencias,
                                                     List<ValorNutricionalIngrediente> valoresNutricionais) {
        validarExigencias(exigencias);

        List<ResultadoAvaliacaoNutriente> resultados = new ArrayList<>();

        for (ExigenciaNutricional exigencia : exigencias) {
            validarExigencia(exigencia);

            BigDecimal valorCalculado = calculadoraFormula.calcularNutrienteNaFormula(
                    formula,
                    exigencia.getNutriente(),
                    valoresNutricionais
            );

            boolean atende = calculadoraFormula.atendeExigencia(valorCalculado, exigencia);

            ResultadoAvaliacaoNutriente resultado = new ResultadoAvaliacaoNutriente(
                    exigencia.getNutriente(),
                    valorCalculado,
                    exigencia.getValorMinimo(),
                    exigencia.getValorMaximo(),
                    atende
            );

            resultados.add(resultado);
        }

        return resultados;
    }

    private void validarExigencias(List<ExigenciaNutricional> exigencias) {
        if (exigencias == null) {
            throw new IllegalArgumentException("Lista de exigências não pode ser nula");
        }

        if (exigencias.isEmpty()) {
            throw new IllegalArgumentException("Não há exigências nutricionais");
        }
    }

    private void validarExigencia(ExigenciaNutricional exigencia) {
        if (exigencia == null) {
            throw new IllegalArgumentException("Exigência nutricional não pode ser nula");
        }
    }
}
