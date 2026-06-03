package com.formularac.dominio;

import java.math.BigDecimal;

public class Ingrediente {

    private Long id;
    private String nome;
    private String categoria;
    private BigDecimal precoPorKg;
    private BigDecimal percentualMinimo;
    private BigDecimal percentualMaximo;
    private Boolean ativo;

    public Ingrediente() {
    }

    public Ingrediente(Long id, String nome, String categoria, BigDecimal precoPorKg,
                       BigDecimal percentualMinimo, BigDecimal percentualMaximo, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.precoPorKg = precoPorKg;
        this.percentualMinimo = percentualMinimo;
        this.percentualMaximo = percentualMaximo;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecoPorKg() {
        return precoPorKg;
    }

    public void setPrecoPorKg(BigDecimal precoPorKg) {
        this.precoPorKg = precoPorKg;
    }

    public BigDecimal getPercentualMinimo() {
        return percentualMinimo;
    }

    public void setPercentualMinimo(BigDecimal percentualMinimo) {
        this.percentualMinimo = percentualMinimo;
    }

    public BigDecimal getPercentualMaximo() {
        return percentualMaximo;
    }

    public void setPercentualMaximo(BigDecimal percentualMaximo) {
        this.percentualMaximo = percentualMaximo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
