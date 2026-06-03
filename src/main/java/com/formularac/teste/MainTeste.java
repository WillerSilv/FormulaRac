package com.formularac.teste;

import com.formularac.dominio.ExigenciaNutricional;
import com.formularac.dominio.Formula;
import com.formularac.dominio.Ingrediente;
import com.formularac.dominio.ItemFormula;
import com.formularac.dominio.Nutriente;
import com.formularac.dominio.ValorNutricionalIngrediente;
import com.formularac.repositorio.RepositorioExigenciasAves;
import com.formularac.servico.CalculadoraFormula;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MainTeste {

    public static void main(String[] args) {

        Ingrediente milho = new Ingrediente(
                1L,
                "Milho",
                "Energetico",
                new BigDecimal("1.20"),
                new BigDecimal("0"),
                new BigDecimal("70"),
                true
        );

        Ingrediente soja = new Ingrediente(
                2L,
                "Farelo de Soja",
                "Proteico",
                new BigDecimal("2.50"),
                new BigDecimal("0"),
                new BigDecimal("40"),
                true
        );

        ItemFormula itemMilho = new ItemFormula(
                1L,
                milho,
                new BigDecimal("70"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        ItemFormula itemSoja = new ItemFormula(
                2L,
                soja,
                new BigDecimal("30"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        Nutriente proteinaBruta = new Nutriente(
                1L,
                "Proteina Bruta",
                "PB",
                "%",
                true
        );

        Nutriente energiaMetabolizavel = new Nutriente(
                2L,
                "Energia Metabolizavel",
                "EM",
                "kcal/kg",
                true
        );

        ValorNutricionalIngrediente proteinaMilho = new ValorNutricionalIngrediente(
                1L,
                milho,
                proteinaBruta,
                new BigDecimal("8")
        );

        ValorNutricionalIngrediente proteinaSoja = new ValorNutricionalIngrediente(
                2L,
                soja,
                proteinaBruta,
                new BigDecimal("45")
        );

        ValorNutricionalIngrediente energiaMilho = new ValorNutricionalIngrediente(
                3L,
                milho,
                energiaMetabolizavel,
                new BigDecimal("3300")
        );

        ValorNutricionalIngrediente energiaSoja = new ValorNutricionalIngrediente(
                4L,
                soja,
                energiaMetabolizavel,
                new BigDecimal("2400")
        );

        List<ItemFormula> itens = new ArrayList<>();
        itens.add(itemMilho);
        itens.add(itemSoja);

        List<ValorNutricionalIngrediente> valoresNutricionais = new ArrayList<>();
        valoresNutricionais.add(proteinaMilho);
        valoresNutricionais.add(proteinaSoja);
        valoresNutricionais.add(energiaMilho);
        valoresNutricionais.add(energiaSoja);

        CalculadoraFormula calculadora = new CalculadoraFormula();
        RepositorioExigenciasAves repositorioExigencias = new RepositorioExigenciasAves(
                proteinaBruta,
                energiaMetabolizavel
        );

        Formula formula = calculadora.calcularManualFormula(
                "Racao Frango Inicial",
                "Frango de Corte",
                "Inicial",
                new BigDecimal("1000"),
                itens
        );

        System.out.println("Formula: " + formula.getNome());
        System.out.println("Ave: " + formula.getTipoAve());
        System.out.println("Fase: " + formula.getFase());
        System.out.println("Lote: " + formula.getTamanhoLoteKg() + " kg");
        System.out.println("Custo total: R$ " + formula.getCustoTotal());

        System.out.println("Itens da formula:");

        for (ItemFormula item : formula.getItens()) {
            System.out.println(
                    item.getIngrediente().getNome()
                            + " | Inclusao: " + item.getPercentualInclusao() + "%"
                            + " | Quantidade: " + item.getQuantidadeKg() + " kg"
                            + " | Custo: R$ " + item.getCusto()
            );
        }

        System.out.println();
        System.out.println("Nutriente: " + proteinaBruta.getNome());
        System.out.println(proteinaMilho.getIngrediente().getNome() + ": " + proteinaMilho.getValor() + proteinaBruta.getUnidade());
        System.out.println(proteinaSoja.getIngrediente().getNome() + ": " + proteinaSoja.getValor() + proteinaBruta.getUnidade());
        System.out.println();
        System.out.println("Nutriente: " + energiaMetabolizavel.getNome());
        System.out.println(energiaMilho.getIngrediente().getNome() + ": " + energiaMilho.getValor() + " " + energiaMetabolizavel.getUnidade());
        System.out.println(energiaSoja.getIngrediente().getNome() + ": " + energiaSoja.getValor() + " " + energiaMetabolizavel.getUnidade());

        BigDecimal proteinaFinal = calculadora.calcularNutrienteNaFormula(
                formula,
                proteinaBruta,
                valoresNutricionais
        );

        BigDecimal energiaFinal = calculadora.calcularNutrienteNaFormula(
                formula,
                energiaMetabolizavel,
                valoresNutricionais
        );

        ExigenciaNutricional exigenciaProteina = repositorioExigencias.buscarPorTipoAveFaseENutriente(
                formula.getTipoAve(),
                formula.getFase(),
                proteinaBruta
        );
        ExigenciaNutricional exigenciaEnergia = repositorioExigencias.buscarPorTipoAveFaseENutriente(
                formula.getTipoAve(),
                formula.getFase(),
                energiaMetabolizavel
        );

        if (exigenciaProteina == null || exigenciaEnergia == null) {
            throw new IllegalStateException("Exigencias nutricionais nao cadastradas para o tipo/fase informado");
        }

        boolean atendeProteina = calculadora.atendeExigencia(proteinaFinal, exigenciaProteina);
        boolean atendeEnergia = calculadora.atendeExigencia(energiaFinal, exigenciaEnergia);

        System.out.println("Proteina final: " + proteinaFinal + proteinaBruta.getUnidade());
        System.out.println("Energia final: " + energiaFinal + " " + energiaMetabolizavel.getUnidade());
        System.out.println("Exigencia minima PB: " + exigenciaProteina.getValorMinimo() + proteinaBruta.getUnidade());
        System.out.println("Exigencia minima EM: " + exigenciaEnergia.getValorMinimo() + " " + energiaMetabolizavel.getUnidade());
        System.out.println("Atende exigencia de proteina: " + atendeProteina);
        System.out.println("Atende exigencia de energia: " + atendeEnergia);
    }
}
