package com.formularac.dominio;

public class Nutriente {

    private Long id;
    private String nome;
    private String abreviacao;
    private String unidade;
    private Boolean ativo;

    public Nutriente() {
    }

    public Nutriente(Long id, String nome, String abreviacao, String unidade, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.abreviacao = abreviacao;
        this.unidade = unidade;
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

    public String getAbreviacao() {
        return abreviacao;
    }

    public void setAbreviacao(String abreviacao) {
        this.abreviacao = abreviacao;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
