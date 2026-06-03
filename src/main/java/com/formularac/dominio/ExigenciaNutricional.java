package com.formularac.dominio;

import java.math.BigDecimal;

public class ExigenciaNutricional {

    private Long id;
    private String tipoAve;
    private String fase;
    private Nutriente nutriente;
    private BigDecimal valorMinimo;
    private BigDecimal valorMaximo;

    public ExigenciaNutricional() {
    }

    public ExigenciaNutricional(Long id, String tipoAve, String fase, Nutriente nutriente,
                                BigDecimal valorMinimo, BigDecimal valorMaximo) {
        this.id = id;
        this.tipoAve = tipoAve;
        this.fase = fase;
        this.nutriente = nutriente;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoAve() {
        return tipoAve;
    }

    public void setTipoAve(String tipoAve) {
        this.tipoAve = tipoAve;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public Nutriente getNutriente() {
        return nutriente;
    }

    public void setNutriente(Nutriente nutriente) {
        this.nutriente = nutriente;
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
}
