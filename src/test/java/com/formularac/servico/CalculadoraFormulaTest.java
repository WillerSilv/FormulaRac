package com.formularac.servico;

import com.formularac.dominio.Formula;
import com.formularac.dominio.Ingrediente;
import com.formularac.dominio.ItemFormula;
import com.formularac.dominio.Nutriente;
import com.formularac.dominio.ValorNutricionalIngrediente;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalculadoraFormulaTest {

    @Test
    void deveCalcularNutrienteQuandoObjetosDiferentesTiveremMesmoId() {
        Ingrediente ingredienteDaFormula = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("1.20"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        Ingrediente ingredienteDoValorNutricional = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("1.20"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        Nutriente nutrientePesquisado = new Nutriente(
                1L,
                "Proteina Bruta",
                "PB",
                "%",
                true
        );

        Nutriente nutrienteDoValorNutricional = new Nutriente(
                1L,
                "Proteina Bruta",
                "PB",
                "%",
                true
        );

        ItemFormula itemFormula = new ItemFormula(
                1L,
                ingredienteDaFormula,
                new BigDecimal("70"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        Formula formula = new Formula(
                1L,
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                BigDecimal.ZERO,
                List.of(itemFormula)
        );

        ValorNutricionalIngrediente valorNutricional = new ValorNutricionalIngrediente(
                1L,
                ingredienteDoValorNutricional,
                nutrienteDoValorNutricional,
                new BigDecimal("8")
        );

        CalculadoraFormula calculadora = new CalculadoraFormula();

        BigDecimal resultado = calculadora.calcularNutrienteNaFormula(
                formula,
                nutrientePesquisado,
                List.of(valorNutricional)
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("5.6"));
    }
}
