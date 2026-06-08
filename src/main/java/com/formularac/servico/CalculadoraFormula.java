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

    // Calcula uma formula informada manualmente pelo usuario.
    // Neste metodo ainda nao existe otimizacao: os percentuais ja chegam definidos.
    public Formula calcularManualFormula(String nome, String tipoAve, String fase, BigDecimal tamanhoLoteKg, List<ItemFormula> itens) {
        validarFormulaManual(tamanhoLoteKg, itens);

        BigDecimal custoTotal = BigDecimal.ZERO;

        for (ItemFormula item : itens) {
            // Exemplo: lote 1000 kg * inclusao 70% / 100 = 700 kg do ingrediente.
            BigDecimal quantidadeKg = tamanhoLoteKg
                    .multiply(item.getPercentualInclusao())
                    .divide(new BigDecimal("100"));

            BigDecimal custo = quantidadeKg.multiply(item.getIngrediente().getPrecoPorKg());

            // Guarda o resultado calculado dentro do proprio item da formula.
            item.setQuantidadeKg(quantidadeKg);
            item.setCusto(custo);

            custoTotal = custoTotal.add(custo);
        }

        return new Formula(null, nome, tipoAve, fase, tamanhoLoteKg, custoTotal, itens);

    }

    // Calcula quanto de um nutriente existe na formula final.
    // Exemplo: milho 70% com 8% PB contribui com 5,6% PB na formula.
    public BigDecimal calcularNutrienteNaFormula(Formula formula, Nutriente nutriente, List<ValorNutricionalIngrediente> valoresNutricionais) {
        validarDadosCalculoNutriente(formula, nutriente, valoresNutricionais);

        BigDecimal valorFinal = BigDecimal.ZERO;

        for (ItemFormula item : formula.getItens()) {
            validarItemCalculoNutriente(item);

            for (ValorNutricionalIngrediente valorNutricional : valoresNutricionais) {
                validarValorNutricional(valorNutricional);

                // Usa o id para comparar, porque podem existir objetos diferentes representando o mesmo ingrediente/nutriente.
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

    // Valida os dados gerais da formula antes de calcular quantidade e custo.
    private void validarFormulaManual(BigDecimal tamanhoLoteKg, List<ItemFormula> itens) {
        if (itens == null) {
            throw new IllegalArgumentException("Lista de itens não pode ser nula");
        }

        if (tamanhoLoteKg == null) {
            throw new IllegalArgumentException("Tamanho do lote não pode ser nulo");
        }

        if (itens.isEmpty()) {
            throw new IllegalArgumentException("Não há itens");
        }

        if (tamanhoLoteKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O tamanho do lote deve ser maior que 0");
        }

        BigDecimal somaPercentual = BigDecimal.ZERO;

        for (ItemFormula item : itens) {
            validarItemFormulaManual(item);
            somaPercentual = somaPercentual.add(item.getPercentualInclusao());
        }

        // Em uma formula manual completa, a soma dos ingredientes precisa fechar exatamente 100%.
        if (somaPercentual.compareTo(new BigDecimal("100")) != 0) {
            throw new IllegalArgumentException("A soma dos percentuais deve ser igual a 100");
        }
    }

    // Valida cada ingrediente informado dentro da formula manual.
    private void validarItemFormulaManual(ItemFormula item) {
        if (item == null) {
            throw new IllegalArgumentException("Item da formula não pode ser nulo");
        }

        if (item.getIngrediente() == null) {
            throw new IllegalArgumentException("Item da formula deve possuir ingrediente");
        }

        if (item.getPercentualInclusao() == null) {
            throw new IllegalArgumentException("Item da formula deve possuir percentual");
        }

        if (item.getIngrediente().getPrecoPorKg() == null) {
            throw new IllegalArgumentException("Ingrediente deve possuir preço por kilo");
        }

        if (item.getIngrediente().getPrecoPorKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero");
        }

        BigDecimal percentual = item.getPercentualInclusao();

        if (percentual.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Percentual não pode ser menor que 0%");
        }

        if (percentual.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("Percentual não pode ser maior que 100%");
        }
    }

    // Valida os parametros necessarios para calcular um nutriente na formula.
    private void validarDadosCalculoNutriente(Formula formula, Nutriente nutriente, List<ValorNutricionalIngrediente> valoresNutricionais) {
        if (formula == null) {
            throw new IllegalArgumentException("A formula não pode ser nula");
        }

        if (formula.getItens() == null) {
            throw new IllegalArgumentException("Os itens da formula não podem ser nulos");
        }

        if (nutriente == null) {
            throw new IllegalArgumentException("O nutriente não pode ser nulo");
        }

        if (valoresNutricionais == null) {
            throw new IllegalArgumentException("Os valores nutricionais não podem ser nulos");
        }
    }

    // Valida o item antes de usar seus dados no calculo nutricional.
    private void validarItemCalculoNutriente(ItemFormula item) {
        if (item == null) {
            throw new IllegalArgumentException("Item da fórmula não pode ser nulo");
        }
    }

    // Valida o valor nutricional cadastrado para um ingrediente.
    private void validarValorNutricional(ValorNutricionalIngrediente valorNutricional) {
        if (valorNutricional == null) {
            throw new IllegalArgumentException("Valor nutricional não pode ser nulo");
        }

        if (valorNutricional.getValor() == null) {
            throw new IllegalArgumentException("Valor do nutriente não pode ser nulo");
        }

        if (valorNutricional.getValor().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor do nutriente não pode ser negativo");
        }
    }

    // Compara ingredientes pelo id, nao pela referencia do objeto em memoria.
    private boolean mesmoIngrediente(Ingrediente ingrediente1, Ingrediente ingrediente2) {
        if (ingrediente1 == null || ingrediente2 == null) {
            return false;
        }

        if (ingrediente1.getId() == null || ingrediente2.getId() == null) {
            return false;
        }

        return ingrediente1.getId().equals(ingrediente2.getId());
    }

    // Compara nutrientes pelo id, nao pela referencia do objeto em memoria.
    private boolean mesmoNutriente(Nutriente nutriente1, Nutriente nutriente2) {
        if (nutriente1 == null || nutriente2 == null) {
            return false;
        }

        if (nutriente1.getId() == null || nutriente2.getId() == null) {
            return false;
        }

        return nutriente1.getId().equals(nutriente2.getId());
    }

    // Verifica se o valor calculado respeita os limites minimo e maximo da exigencia.
    public boolean atendeExigencia(BigDecimal valorCalculado, ExigenciaNutricional exigencia) {
        validarAtendimentoExigencia(valorCalculado, exigencia);

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

    // Garante que existe pelo menos um limite nutricional para validar.
    private void validarAtendimentoExigencia(BigDecimal valorCalculado, ExigenciaNutricional exigencia) {
        if (valorCalculado == null) {
            throw new IllegalArgumentException("Valor calculado nao pode ser nulo");
        }

        if (exigencia == null) {
            throw new IllegalArgumentException("Exigencia nutricional nao pode ser nula");
        }

        if (exigencia.getValorMinimo() == null && exigencia.getValorMaximo() == null) {
            throw new IllegalArgumentException("Exigência deve possuir valor minimo ou máximo");
        }
    }

}
