package com.formularac.servico;

import com.formularac.dominio.ExigenciaNutricional;
import com.formularac.dominio.Formula;
import com.formularac.dominio.Ingrediente;
import com.formularac.dominio.ItemFormula;
import com.formularac.dominio.Nutriente;
import com.formularac.dominio.ValorNutricionalIngrediente;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalculadoraFormulaTest {

    // Testes do cálculo de nutriente na fórmula.

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

    // Validações da fórmula manual.

    @Test
    void deveLancarErroQuandoListaDeItensForNula() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Lista de itens não pode ser nula");
    }

    @Test
    void deveLancarErroQuandoTamanhoDoLoteForNulo() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                null,
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Tamanho do lote não pode ser nulo");
    }

    @Test
    void deveLancarErroQuandoListaDeItensForVazia() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Não há itens");
    }

    @Test
    void deveLancarErroQuandoTamanhoDoLoteForMenorOuIgualAZero() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                BigDecimal.ZERO,
                List.of(new ItemFormula(
                        1L,
                        new Ingrediente(
                                1L,
                                "Milho",
                                "Energetico",
                                new BigDecimal("1.20"),
                                BigDecimal.ZERO,
                                new BigDecimal("100"),
                                true
                        ),
                        new BigDecimal("100"),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                ))
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O tamanho do lote deve ser maior que 0");
    }

    @Test
    void deveLancarErroQuandoTamanhoDoLoteForNegativo() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("-1"),
                List.of(itemFormulaMilho(new BigDecimal("100")))
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O tamanho do lote deve ser maior que 0");
    }

    @Test
    void deveLancarErroQuandoItemDaFormulaForNulo() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                Collections.singletonList((ItemFormula) null)
        ))
                .isInstanceOf(IllegalArgumentException.class).hasMessage("Item da formula não pode ser nulo");
    }

    @Test
    void deveLancarErroQuandoItemNaoPossuirIngrediente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        ItemFormula itemSemIngrediente = new ItemFormula(
                1L,
                null,
                new BigDecimal("100"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of(itemSemIngrediente)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Item da formula deve possuir ingrediente");
    }

    @Test
    void deveLancarErroQuandoItemNaoPossuirPercentual() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Ingrediente milho = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("1.20"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );
        ItemFormula itemSemPercentual = new ItemFormula(
                1L,
                milho,
                null,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of(itemSemPercentual)
        ))
            .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Item da formula deve possuir percentual");
    }

    @Test
    void deveLancarErroQuandoItemNaoPossuirPrecoPorKg() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Ingrediente milhoSemPreco = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                null,
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        ItemFormula item = new ItemFormula(
                1L,
                milhoSemPreco,
                new BigDecimal("100"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of(item)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ingrediente deve possuir preço por kilo");
    }

    @Test
    void deveLancarErroQuandoPrecoPorKgForMenorOuIgualAZero() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Ingrediente milhoComPrecoZero = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        ItemFormula item = new ItemFormula(
                1L,
                milhoComPrecoZero,
                new BigDecimal("100"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of(item)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O preço deve ser maior que zero");
    }

    @Test
    void deveLancarErroQuandoPrecoPorKgForNegativo() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Ingrediente milhoComPrecoNegativo = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("-1"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        ItemFormula item = new ItemFormula(
                1L,
                milhoComPrecoNegativo,
                new BigDecimal("100"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of(item)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O preço deve ser maior que zero");
    }

    @Test
    void deveLancarErroQuandoPercentualForMenorQueZero() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Ingrediente milho = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("1.20"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        ItemFormula item = new ItemFormula(
                1L,
                milho,
                new BigDecimal("-1"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of(item)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Percentual não pode ser menor que 0%");
    }

    @Test
    void deveLancarErroQuandoPercentualForMaiorQueCem() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Ingrediente milho = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("1.20"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        ItemFormula item = new ItemFormula(
                1L,
                milho,
                new BigDecimal("110"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of(item)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Percentual não pode ser maior que 100%");
    }

    @Test
    void deveLancarErroQuandoSomaDosPercentuaisForDiferenteDeCem() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Ingrediente milho = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("1.20"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        ItemFormula item = new ItemFormula(
                1L,
                milho,
                new BigDecimal("90"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertThatThrownBy(() -> calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of(item)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A soma dos percentuais deve ser igual a 100");
    }

    // Validações do cálculo de nutriente na fórmula.

    @Test
    void deveLancarErroQuandoFormulaForNulaNoCalculoDeNutriente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularNutrienteNaFormula(
                null,
                proteinaBruta(),
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A formula não pode ser nula");
    }

    @Test
    void deveLancarErroQuandoItensDaFormulaForemNulosNoCalculoDeNutriente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Formula formula = formulaComItens(null);

        assertThatThrownBy(() -> calculadora.calcularNutrienteNaFormula(
                formula,
                proteinaBruta(),
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Os itens da formula não podem ser nulos");
    }

    @Test
    void deveLancarErroQuandoNutrienteForNuloNoCalculoDeNutriente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularNutrienteNaFormula(
                formulaValidaParaCalculoNutriente(),
                null,
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O nutriente não pode ser nulo");
    }

    @Test
    void deveLancarErroQuandoValoresNutricionaisForemNulosNoCalculoDeNutriente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularNutrienteNaFormula(
                formulaValidaParaCalculoNutriente(),
                proteinaBruta(),
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Os valores nutricionais não podem ser nulos");
    }

    @Test
    void deveLancarErroQuandoItemForNuloNoCalculoDeNutriente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Formula formula = formulaComItens(Collections.singletonList((ItemFormula) null));

        assertThatThrownBy(() -> calculadora.calcularNutrienteNaFormula(
                formula,
                proteinaBruta(),
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Item da fórmula não pode ser nulo");
    }

    @Test
    void deveLancarErroQuandoValorNutricionalForNuloNoCalculoDeNutriente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularNutrienteNaFormula(
                formulaValidaParaCalculoNutriente(),
                proteinaBruta(),
                Collections.singletonList((ValorNutricionalIngrediente) null)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor nutricional não pode ser nulo");
    }

    @Test
    void deveLancarErroQuandoValorDoNutrienteForNuloNoCalculoDeNutriente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularNutrienteNaFormula(
                formulaValidaParaCalculoNutriente(),
                proteinaBruta(),
                List.of(valorNutricionalComValor(null))
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor do nutriente não pode ser nulo");
    }

    @Test
    void deveLancarErroQuandoValorDoNutrienteForNegativoNoCalculoDeNutriente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.calcularNutrienteNaFormula(
                formulaValidaParaCalculoNutriente(),
                proteinaBruta(),
                List.of(valorNutricionalComValor(new BigDecimal("-1")))
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor do nutriente não pode ser negativo");
    }

    // Validações da comparação com as exigências nutricionais.

    @Test
    void deveLancarErroQuandoValorCalculadoForNuloAoVerificarExigencia() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.atendeExigencia(
                null,
                exigenciaProteina(new BigDecimal("20"), new BigDecimal("24"))
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor calculado nao pode ser nulo");
    }

    @Test
    void deveLancarErroQuandoExigenciaForNulaAoVerificarExigencia() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.atendeExigencia(
                new BigDecimal("21"),
                null
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Exigencia nutricional nao pode ser nula");
    }

    @Test
    void deveLancarErroQuandoExigenciaNaoPossuirValorMinimoNemValorMaximo() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        assertThatThrownBy(() -> calculadora.atendeExigencia(
                new BigDecimal("21"),
                exigenciaProteina(null, null)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Exigência deve possuir valor minimo ou máximo");
    }

    // Testes de resultado, verificando os cálculos e retornos esperados.

    @Test
    void deveCalcularQuantidadeECustoDaFormulaManual() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        // Cenário: dois ingredientes fechando 100% de uma fórmula de 1000 kg.
        Ingrediente milho = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("1.20"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        Ingrediente fareloSoja = new Ingrediente(
                2L,
                "Farelo de Soja",
                "Proteico",
                new BigDecimal("2.00"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );

        ItemFormula itemMilho = new ItemFormula(
                1L,
                milho,
                new BigDecimal("70"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        ItemFormula itemFareloSoja = new ItemFormula(
                2L,
                fareloSoja,
                new BigDecimal("30"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        // Execução: calcula quantidade em kg e custo de cada ingrediente.
        Formula formula = calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                List.of(itemMilho, itemFareloSoja)
        );

        // Verificação: confere custo total, quantidade e custo de cada item.
        assertThat(formula.getCustoTotal()).isEqualByComparingTo(new BigDecimal("1440"));
        assertThat(itemMilho.getQuantidadeKg()).isEqualByComparingTo(new BigDecimal("700"));
        assertThat(itemMilho.getCusto()).isEqualByComparingTo(new BigDecimal("840"));
        assertThat(itemFareloSoja.getQuantidadeKg()).isEqualByComparingTo(new BigDecimal("300"));
        assertThat(itemFareloSoja.getCusto()).isEqualByComparingTo(new BigDecimal("600"));
    }

    @Test
    void devePreencherDadosDaFormulaManual() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        List<ItemFormula> itens = List.of(itemFormulaMilho(new BigDecimal("100")));

        Formula formula = calculadora.calcularManualFormula(
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                itens
        );

        assertThat(formula.getNome()).isEqualTo("Formula Teste");
        assertThat(formula.getTipoAve()).isEqualTo("Frango de Corte");
        assertThat(formula.getFase()).isEqualTo("Inicial");
        assertThat(formula.getTamanhoLoteKg()).isEqualByComparingTo(new BigDecimal("1000"));
        assertThat(formula.getItens()).isEqualTo(itens);
    }

    @Test
    void deveCalcularNutrienteSomandoContribuicoesDeMaisDeUmIngrediente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Ingrediente milho = milho();
        Ingrediente fareloSoja = fareloSoja();
        Nutriente proteinaBruta = proteinaBruta();

        ItemFormula itemMilho = new ItemFormula(
                1L,
                milho,
                new BigDecimal("70"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        ItemFormula itemFareloSoja = new ItemFormula(
                2L,
                fareloSoja,
                new BigDecimal("30"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        Formula formula = formulaComItens(List.of(itemMilho, itemFareloSoja));

        ValorNutricionalIngrediente valorMilho = new ValorNutricionalIngrediente(
                1L,
                milho,
                proteinaBruta,
                new BigDecimal("8")
        );

        ValorNutricionalIngrediente valorFareloSoja = new ValorNutricionalIngrediente(
                2L,
                fareloSoja,
                proteinaBruta,
                new BigDecimal("45")
        );

        // Milho: 70% * 8 / 100 = 5.6
        // Farelo de soja: 30% * 45 / 100 = 13.5
        // Total: 19.1
        BigDecimal resultado = calculadora.calcularNutrienteNaFormula(
                formula,
                proteinaBruta,
                List.of(valorMilho, valorFareloSoja)
        );

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("19.1"));
    }

    @Test
    void deveRetornarZeroQuandoNaoEncontrarValorNutricionalCorrespondente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        Nutriente energiaMetabolizavel = new Nutriente(
                2L,
                "Energia Metabolizavel",
                "EM",
                "kcal/kg",
                true
        );

        BigDecimal resultado = calculadora.calcularNutrienteNaFormula(
                formulaValidaParaCalculoNutriente(),
                energiaMetabolizavel,
                List.of(valorNutricionalComValor(new BigDecimal("8")))
        );

        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void deveRetornarZeroQuandoValorNutricionalNaoPossuirIngrediente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        ValorNutricionalIngrediente valorNutricionalSemIngrediente = new ValorNutricionalIngrediente(
                1L,
                null,
                proteinaBruta(),
                new BigDecimal("8")
        );

        BigDecimal resultado = calculadora.calcularNutrienteNaFormula(
                formulaValidaParaCalculoNutriente(),
                proteinaBruta(),
                List.of(valorNutricionalSemIngrediente)
        );

        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void deveRetornarZeroQuandoValorNutricionalNaoPossuirNutriente() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        ValorNutricionalIngrediente valorNutricionalSemNutriente = new ValorNutricionalIngrediente(
                1L,
                milho(),
                null,
                new BigDecimal("8")
        );

        BigDecimal resultado = calculadora.calcularNutrienteNaFormula(
                formulaValidaParaCalculoNutriente(),
                proteinaBruta(),
                List.of(valorNutricionalSemNutriente)
        );

        assertThat(resultado).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void deveRetornarVerdadeiroQuandoValorCalculadoAtenderExigencia() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        boolean atende = calculadora.atendeExigencia(
                new BigDecimal("21"),
                exigenciaProteina(new BigDecimal("20"), new BigDecimal("24"))
        );

        assertThat(atende).isTrue();
    }

    @Test
    void deveRetornarFalsoQuandoValorCalculadoFicarAbaixoDoMinimo() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        boolean atende = calculadora.atendeExigencia(
                new BigDecimal("19"),
                exigenciaProteina(new BigDecimal("20"), new BigDecimal("24"))
        );

        assertThat(atende).isFalse();
    }

    @Test
    void deveRetornarFalsoQuandoValorCalculadoFicarAcimaDoMaximo() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        boolean atende = calculadora.atendeExigencia(
                new BigDecimal("25"),
                exigenciaProteina(new BigDecimal("20"), new BigDecimal("24"))
        );

        assertThat(atende).isFalse();
    }

    @Test
    void deveRetornarVerdadeiroQuandoValorCalculadoForIgualAosLimites() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        boolean atendeValorMinimo = calculadora.atendeExigencia(
                new BigDecimal("20"),
                exigenciaProteina(new BigDecimal("20"), new BigDecimal("24"))
        );

        boolean atendeValorMaximo = calculadora.atendeExigencia(
                new BigDecimal("24"),
                exigenciaProteina(new BigDecimal("20"), new BigDecimal("24"))
        );

        assertThat(atendeValorMinimo).isTrue();
        assertThat(atendeValorMaximo).isTrue();
    }

    @Test
    void deveRetornarVerdadeiroQuandoExigenciaPossuirApenasValorMinimoEValorCalculadoForMaior() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        boolean atende = calculadora.atendeExigencia(
                new BigDecimal("21"),
                exigenciaProteina(new BigDecimal("20"), null)
        );

        assertThat(atende).isTrue();
    }

    @Test
    void deveRetornarFalsoQuandoExigenciaPossuirApenasValorMinimoEValorCalculadoForMenor() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        boolean atende = calculadora.atendeExigencia(
                new BigDecimal("19"),
                exigenciaProteina(new BigDecimal("20"), null)
        );

        assertThat(atende).isFalse();
    }

    @Test
    void deveRetornarVerdadeiroQuandoExigenciaPossuirApenasValorMaximoEValorCalculadoForMenor() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        boolean atende = calculadora.atendeExigencia(
                new BigDecimal("21"),
                exigenciaProteina(null, new BigDecimal("24"))
        );

        assertThat(atende).isTrue();
    }

    @Test
    void deveRetornarFalsoQuandoExigenciaPossuirApenasValorMaximoEValorCalculadoForMaior() {
        CalculadoraFormula calculadora = new CalculadoraFormula();

        boolean atende = calculadora.atendeExigencia(
                new BigDecimal("25"),
                exigenciaProteina(null, new BigDecimal("24"))
        );

        assertThat(atende).isFalse();
    }

    // Métodos auxiliares para montar objetos usados nos testes.

    private Formula formulaValidaParaCalculoNutriente() {
        return formulaComItens(List.of(itemFormulaMilho(new BigDecimal("70"))));
    }

    private Formula formulaComItens(List<ItemFormula> itens) {
        return new Formula(
                1L,
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                BigDecimal.ZERO,
                itens
        );
    }

    private ItemFormula itemFormulaMilho(BigDecimal percentualInclusao) {
        return new ItemFormula(
                1L,
                milho(),
                percentualInclusao,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    private Ingrediente milho() {
        return new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("1.20"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );
    }

    private Ingrediente fareloSoja() {
        return new Ingrediente(
                2L,
                "Farelo de Soja",
                "Proteico",
                new BigDecimal("2.00"),
                BigDecimal.ZERO,
                new BigDecimal("100"),
                true
        );
    }

    private Nutriente proteinaBruta() {
        return new Nutriente(
                1L,
                "Proteina Bruta",
                "PB",
                "%",
                true
        );
    }

    private ValorNutricionalIngrediente valorNutricionalComValor(BigDecimal valor) {
        return new ValorNutricionalIngrediente(
                1L,
                milho(),
                proteinaBruta(),
                valor
        );
    }

    private ExigenciaNutricional exigenciaProteina(BigDecimal valorMinimo, BigDecimal valorMaximo) {
        return new ExigenciaNutricional(
                1L,
                "Frango de Corte",
                "Inicial",
                proteinaBruta(),
                valorMinimo,
                valorMaximo
        );
    }

}
