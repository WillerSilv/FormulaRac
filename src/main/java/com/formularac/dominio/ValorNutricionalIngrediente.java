package com.formularac.dominio;

import java.math.BigDecimal;

public class ValorNutricionalIngrediente {

    private Long id;
    private Ingrediente ingrediente;
    private Nutriente nutriente;
    private BigDecimal valor;

    public ValorNutricionalIngrediente() {
    }

    public ValorNutricionalIngrediente(Long id, Ingrediente ingrediente, Nutriente nutriente, BigDecimal valor) {
        this.id = id;
        this.ingrediente = ingrediente;
        this.nutriente = nutriente;
        this.valor = valor;
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

    public Nutriente getNutriente() {
        return nutriente;
    }

    public void setNutriente(Nutriente nutriente) {
        this.nutriente = nutriente;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
