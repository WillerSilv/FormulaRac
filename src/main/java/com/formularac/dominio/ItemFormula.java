package com.formularac.dominio;

import java.math.BigDecimal;

public class ItemFormula {

    private Long id;
    private Ingrediente ingrediente;
    private BigDecimal percentualInclusao;
    private BigDecimal quantidadeKg;
    private BigDecimal custo;

    public ItemFormula() {
    }

    public ItemFormula(Long id, Ingrediente ingrediente, BigDecimal percentualInclusao,
                       BigDecimal quantidadeKg, BigDecimal custo) {
        this.id = id;
        this.ingrediente = ingrediente;
        this.percentualInclusao = percentualInclusao;
        this.quantidadeKg = quantidadeKg;
        this.custo = custo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public BigDecimal getPercentualInclusao() {
        return percentualInclusao;
    }

    public void setPercentualInclusao(BigDecimal percentualInclusao) {
        this.percentualInclusao = percentualInclusao;
    }

    public BigDecimal getQuantidadeKg() {
        return quantidadeKg;
    }

    public void setQuantidadeKg(BigDecimal quantidadeKg) {
        this.quantidadeKg = quantidadeKg;
    }

    public BigDecimal getCusto() {
        return custo;
    }

    public void setCusto(BigDecimal custo) {
        this.custo = custo;
    }
}
