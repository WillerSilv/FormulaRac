package com.formularac.dominio;

import java.math.BigDecimal;

public class ResultadoAvaliacaoNutriente {

    private Nutriente nutriente;
    private BigDecimal valorCalculado;
    private BigDecimal valorMinimo;
    private BigDecimal valorMaximo;
    private boolean atende;

    public ResultadoAvaliacaoNutriente() {
    }

    public ResultadoAvaliacaoNutriente(Nutriente nutriente, BigDecimal valorCalculado,
                                       BigDecimal valorMinimo, BigDecimal valorMaximo,
                                       boolean atende) {
        this.nutriente = nutriente;
        this.valorCalculado = valorCalculado;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.atende = atende;
    }

    public Nutriente getNutriente() {
        return nutriente;
    }

    public void setNutriente(Nutriente nutriente) {
        this.nutriente = nutriente;
    }

    public BigDecimal getValorCalculado() {
        return valorCalculado;
    }

    public void setValorCalculado(BigDecimal valorCalculado) {
        this.valorCalculado = valorCalculado;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(BigDecimal valorMinimo) {
        this.valorMinimo = valorMinimo;
    }

    public BigDecimal getValorMaximo() {
        return valorMaximo;
    }

    public void setValorMaximo(BigDecimal valorMaximo) {
        this.valorMaximo = valorMaximo;
    }

    public boolean isAtende() {
        return atende;
    }

    public void setAtende(boolean atende) {
        this.atende = atende;
    }
}
