package com.formularac.servico;

import com.formularac.dominio.ExigenciaNutricional;
import com.formularac.dominio.Formula;
import com.formularac.dominio.Ingrediente;
import com.formularac.dominio.ItemFormula;
import com.formularac.dominio.Nutriente;
import com.formularac.dominio.ResultadoAvaliacaoNutriente;
import com.formularac.dominio.ValorNutricionalIngrediente;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AvaliadorFormulaTest {

    @Test
    void deveAvaliarFormulaQuandoNutrienteAtenderExigencia() {
        AvaliadorFormula avaliador = new AvaliadorFormula();
        Nutriente proteinaBruta = proteinaBruta();

        ExigenciaNutricional exigencia = exigenciaProteina(
                proteinaBruta,
                new BigDecimal("18"),
                new BigDecimal("22")
        );

        List<ResultadoAvaliacaoNutriente> resultados = avaliador.avaliar(
                formulaComMilhoEFareloSoja(),
                List.of(exigencia),
                valoresNutricionaisProteina(proteinaBruta)
        );

        ResultadoAvaliacaoNutriente resultado = resultados.get(0);

        assertThat(resultados).hasSize(1);
        assertThat(resultado.getNutriente()).isEqualTo(proteinaBruta);
        assertThat(resultado.getValorCalculado()).isEqualByComparingTo(new BigDecimal("19.1"));
        assertThat(resultado.getValorMinimo()).isEqualByComparingTo(new BigDecimal("18"));
        assertThat(resultado.getValorMaximo()).isEqualByComparingTo(new BigDecimal("22"));
        assertThat(resultado.isAtende()).isTrue();
    }

    @Test
    void deveAvaliarFormulaQuandoNutrienteNaoAtenderExigencia() {
        AvaliadorFormula avaliador = new AvaliadorFormula();
        Nutriente proteinaBruta = proteinaBruta();

        ExigenciaNutricional exigencia = exigenciaProteina(
                proteinaBruta,
                new BigDecimal("20"),
                new BigDecimal("24")
        );

        List<ResultadoAvaliacaoNutriente> resultados = avaliador.avaliar(
                formulaComMilhoEFareloSoja(),
                List.of(exigencia),
                valoresNutricionaisProteina(proteinaBruta)
        );

        ResultadoAvaliacaoNutriente resultado = resultados.get(0);

        assertThat(resultados).hasSize(1);
        assertThat(resultado.getValorCalculado()).isEqualByComparingTo(new BigDecimal("19.1"));
        assertThat(resultado.getValorMinimo()).isEqualByComparingTo(new BigDecimal("20"));
        assertThat(resultado.getValorMaximo()).isEqualByComparingTo(new BigDecimal("24"));
        assertThat(resultado.isAtende()).isFalse();
    }

    @Test
    void deveAvaliarFormulaComMaisDeUmaExigenciaNutricional() {
        AvaliadorFormula avaliador = new AvaliadorFormula();

        Nutriente proteinaBruta = proteinaBruta();
        Nutriente energiaMetabolizavel = energiaMetabolizavel();

        ExigenciaNutricional exigenciaProteina = exigencia(
                proteinaBruta,
                new BigDecimal("18"),
                new BigDecimal("22")
        );

        ExigenciaNutricional exigenciaEnergia = exigencia(
                energiaMetabolizavel,
                new BigDecimal("3100"),
                null
        );

        List<ResultadoAvaliacaoNutriente> resultados = avaliador.avaliar(
                formulaComMilhoEFareloSoja(),
                List.of(exigenciaProteina, exigenciaEnergia),
                valoresNutricionaisProteinaEEnergia(proteinaBruta, energiaMetabolizavel)
        );

        ResultadoAvaliacaoNutriente resultadoProteina = resultados.get(0);
        ResultadoAvaliacaoNutriente resultadoEnergia = resultados.get(1);

        assertThat(resultados).hasSize(2);

        assertThat(resultadoProteina.getNutriente()).isEqualTo(proteinaBruta);
        assertThat(resultadoProteina.getValorCalculado()).isEqualByComparingTo(new BigDecimal("19.1"));
        assertThat(resultadoProteina.isAtende()).isTrue();

        assertThat(resultadoEnergia.getNutriente()).isEqualTo(energiaMetabolizavel);
        assertThat(resultadoEnergia.getValorCalculado()).isEqualByComparingTo(new BigDecimal("3030"));
        assertThat(resultadoEnergia.isAtende()).isFalse();
    }

    @Test
    void deveLancarErroQuandoListaDeExigenciasForNula() {
        AvaliadorFormula avaliador = new AvaliadorFormula();

        assertThatThrownBy(() -> avaliador.avaliar(
                formulaComMilhoEFareloSoja(),
                null,
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Lista de exigências não pode ser nula");
    }

    @Test
    void deveLancarErroQuandoListaDeExigenciasForVazia() {
        AvaliadorFormula avaliador = new AvaliadorFormula();

        assertThatThrownBy(() -> avaliador.avaliar(
                formulaComMilhoEFareloSoja(),
                List.of(),
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Não há exigências nutricionais");
    }

    @Test
    void deveLancarErroQuandoExigenciaForNula() {
        AvaliadorFormula avaliador = new AvaliadorFormula();

        assertThatThrownBy(() -> avaliador.avaliar(
                formulaComMilhoEFareloSoja(),
                Collections.singletonList((ExigenciaNutricional) null),
                List.of()
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Exigência nutricional não pode ser nula");
    }

    private Formula formulaComMilhoEFareloSoja() {
        ItemFormula itemMilho = new ItemFormula(
                1L,
                milho(),
                new BigDecimal("70"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        ItemFormula itemFareloSoja = new ItemFormula(
                2L,
                fareloSoja(),
                new BigDecimal("30"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        return new Formula(
                1L,
                "Formula Teste",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                BigDecimal.ZERO,
                List.of(itemMilho, itemFareloSoja)
        );
    }

    private List<ValorNutricionalIngrediente> valoresNutricionaisProteina(Nutriente proteinaBruta) {
        ValorNutricionalIngrediente proteinaMilho = new ValorNutricionalIngrediente(
                1L,
                milho(),
                proteinaBruta,
                new BigDecimal("8")
        );

        ValorNutricionalIngrediente proteinaFareloSoja = new ValorNutricionalIngrediente(
                2L,
                fareloSoja(),
                proteinaBruta,
                new BigDecimal("45")
        );

        return List.of(proteinaMilho, proteinaFareloSoja);
    }

    private List<ValorNutricionalIngrediente> valoresNutricionaisProteinaEEnergia(Nutriente proteinaBruta,
                                                                                  Nutriente energiaMetabolizavel) {
        ValorNutricionalIngrediente proteinaMilho = new ValorNutricionalIngrediente(
                1L,
                milho(),
                proteinaBruta,
                new BigDecimal("8")
        );

        ValorNutricionalIngrediente proteinaFareloSoja = new ValorNutricionalIngrediente(
                2L,
                fareloSoja(),
                proteinaBruta,
                new BigDecimal("45")
        );

        ValorNutricionalIngrediente energiaMilho = new ValorNutricionalIngrediente(
                3L,
                milho(),
                energiaMetabolizavel,
                new BigDecimal("3300")
        );

        ValorNutricionalIngrediente energiaFareloSoja = new ValorNutricionalIngrediente(
                4L,
                fareloSoja(),
                energiaMetabolizavel,
                new BigDecimal("2400")
        );

        return List.of(proteinaMilho, proteinaFareloSoja, energiaMilho, energiaFareloSoja);
    }

    private ExigenciaNutricional exigenciaProteina(Nutriente proteinaBruta,
                                                   BigDecimal valorMinimo,
                                                   BigDecimal valorMaximo) {
        return exigencia(proteinaBruta, valorMinimo, valorMaximo);
    }

    private ExigenciaNutricional exigencia(Nutriente nutriente,
                                           BigDecimal valorMinimo,
                                           BigDecimal valorMaximo) {
        return new ExigenciaNutricional(
                1L,
                "Frango de Corte",
                "Inicial",
                nutriente,
                valorMinimo,
                valorMaximo
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

    private Nutriente energiaMetabolizavel() {
        return new Nutriente(
                2L,
                "Energia Metabolizavel",
                "EM",
                "kcal/kg",
                true
        );
    }
}
