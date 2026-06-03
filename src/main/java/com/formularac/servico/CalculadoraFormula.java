package com.formularac.servico;

import com.formularac.dominio.ExigenciaNutricional;
import com.formularac.dominio.Formula;
import com.formularac.dominio.Ingrediente;
import com.formularac.dominio.ItemFormula;
import com.formularac.dominio.Nutriente;
import com.formularac.dominio.ValorNutricionalIngrediente;

import java.math.BigDecimal;
import java.util.List;

public class CalculadoraFormula {
    public Formula calcularManualFormula(String nome, String tipoAve, String fase, BigDecimal tamanhoLoteKg, List<ItemFormula> itens) {
        // Validação
        if(itens.isEmpty()){
            throw new IllegalArgumentException("Não há itens");
        }

        if (tamanhoLoteKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O tamanho do lote deve ser maior que 0");
        }

        BigDecimal somaPercentual = BigDecimal.ZERO;

        for (ItemFormula item : itens){
            if (item.getIngrediente() == null){
                throw new IllegalArgumentException(("Item da formula deve possuir ingrediente"));
            }

            if (item.getPercentualInclusao() == null){
                throw new IllegalArgumentException(("Item da formula deve possuir percentual"));
            }

            if(item.getIngrediente().getPrecoPorKg() == null){
                throw new IllegalArgumentException("Ingrediente deve possuir preço por kilo");
            }

            if(item.getIngrediente().getPrecoPorKg().compareTo(BigDecimal.ZERO) <= 0){
                throw new IllegalArgumentException("O preço deve ser maior que zero");
            }

            BigDecimal percentual = item.getPercentualInclusao();

            if (percentual.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Percentual não pode ser menor que 0%");
            }

            if (percentual.compareTo(new BigDecimal("100")) > 0) {
                throw new IllegalArgumentException("Percentual não pode ser maior que 100");
            }

            somaPercentual = somaPercentual.add(percentual);
        }

        if (somaPercentual.compareTo(new BigDecimal("100")) != 0) {
            throw new IllegalArgumentException("A soma dos percentuais deve ser igual a 100");
        }

        // Calculo
        BigDecimal custoTotal = BigDecimal.ZERO;
        for (ItemFormula item : itens) {
            BigDecimal quantidadeKg = tamanhoLoteKg
                    .multiply(item.getPercentualInclusao())
                    .divide(new BigDecimal("100"));

            BigDecimal custo = quantidadeKg.multiply(item.getIngrediente().getPrecoPorKg());

            item.setQuantidadeKg(quantidadeKg);
            item.setCusto(custo);

            custoTotal = custoTotal.add(custo);
        }

        return new Formula(null, nome, tipoAve, fase, tamanhoLoteKg, custoTotal, itens);

    }

    public BigDecimal calcularNutrienteNaFormula(Formula formula, Nutriente nutriente, List<ValorNutricionalIngrediente> valoresNutricionais) {

        BigDecimal valorFinal = BigDecimal.ZERO;

        for (ItemFormula item : formula.getItens()) {
            for (ValorNutricionalIngrediente valorNutricional : valoresNutricionais) {
                if (mesmoIngrediente(valorNutricional.getIngrediente(), item.getIngrediente())
                        && mesmoNutriente(valorNutricional.getNutriente(), nutriente)) {

                    BigDecimal contribuicao = item.getPercentualInclusao()
                            .multiply(valorNutricional.getValor())
                            .divide(new BigDecimal("100"));

                    valorFinal = valorFinal.add(contribuicao);
                }
            }
        }

        return valorFinal;
    }

    private boolean mesmoIngrediente(Ingrediente ingrediente1, Ingrediente ingrediente2) {
        if (ingrediente1 == null || ingrediente2 == null) {
            return false;
        }

        if (ingrediente1.getId() == null || ingrediente2.getId() == null) {
            return false;
        }

        return ingrediente1.getId().equals(ingrediente2.getId());
    }

    private boolean mesmoNutriente(Nutriente nutriente1, Nutriente nutriente2) {
        if (nutriente1 == null || nutriente2 == null) {
            return false;
        }

        if (nutriente1.getId() == null || nutriente2.getId() == null) {
            return false;
        }

        return nutriente1.getId().equals(nutriente2.getId());
    }

    public boolean atendeExigencia(BigDecimal valorCalculado, ExigenciaNutricional exigencia) {
        if (valorCalculado == null) {
            throw new IllegalArgumentException("Valor calculado nao pode ser nulo");
        }

        if (exigencia == null) {
            throw new IllegalArgumentException("Exigencia nutricional nao pode ser nula");
        }

        if (exigencia.getValorMinimo() != null
                && valorCalculado.compareTo(exigencia.getValorMinimo()) < 0) {
            return false;
        }

        if (exigencia.getValorMaximo() != null
                && valorCalculado.compareTo(exigencia.getValorMaximo()) > 0) {
            return false;
        }

        return true;
    }

}
