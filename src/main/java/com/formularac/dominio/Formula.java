package com.formularac.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Formula {
    private Long id;
    private String nome;
    private String tipoAve;
    private String fase;
    private BigDecimal tamanhoLoteKg;
    private BigDecimal custoTotal;
    private List<ItemFormula> itens = new ArrayList<>();

    public Formula() {
    }

    public Formula(Long id, String nome, String tipoAve, String fase, BigDecimal tamanhoLoteKg, BigDecimal custoTotal, List<ItemFormula> itens) {
        this.id = id;
        this.nome = nome;
        this.tipoAve = tipoAve;
        this.fase = fase;
        this.tamanhoLoteKg = tamanhoLoteKg;
        this.custoTotal = custoTotal;
        this.itens = itens;
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

    public BigDecimal getTamanhoLoteKg() {
        return tamanhoLoteKg;
    }

    public void setTamanhoLoteKg(BigDecimal tamanhoLoteKg) {
        this.tamanhoLoteKg = tamanhoLoteKg;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public void setCustoTotal(BigDecimal custoTotal) {
        this.custoTotal = custoTotal;
    }

    public List<ItemFormula> getItens() {
        return itens;
    }

    public void setItens(List<ItemFormula> itens) {
        this.itens = itens;
    }
}
