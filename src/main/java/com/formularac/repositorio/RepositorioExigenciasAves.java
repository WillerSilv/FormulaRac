package com.formularac.repositorio;

import com.formularac.dominio.ExigenciaNutricional;
import com.formularac.dominio.Nutriente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RepositorioExigenciasAves {

    private final List<ExigenciaNutricional> exigencias = new ArrayList<>();

    public RepositorioExigenciasAves(Nutriente proteinaBruta, Nutriente energiaMetabolizavel) {
        carregarFrangoCorteMachoRegularMedio(proteinaBruta, energiaMetabolizavel);
    }

    public List<ExigenciaNutricional> buscarPorTipoAveEFase(String tipoAve, String fase) {
        List<ExigenciaNutricional> resultado = new ArrayList<>();

        for (ExigenciaNutricional exigencia : exigencias) {
            if (textoIgual(exigencia.getTipoAve(), tipoAve)
                    && textoIgual(exigencia.getFase(), fase)) {
                resultado.add(exigencia);
            }
        }

        return resultado;
    }

    public ExigenciaNutricional buscarPorTipoAveFaseENutriente(String tipoAve, String fase, Nutriente nutriente) {
        for (ExigenciaNutricional exigencia : exigencias) {
            if (textoIgual(exigencia.getTipoAve(), tipoAve)
                    && textoIgual(exigencia.getFase(), fase)
                    && mesmoNutriente(exigencia.getNutriente(), nutriente)) {
                return exigencia;
            }
        }

        return null;
    }

    private void carregarFrangoCorteMachoRegularMedio(Nutriente proteinaBruta, Nutriente energiaMetabolizavel) {
        // Base TBAS 2017 - Tabela 2.20 (frangos de corte machos, desempenho regular-medio)
        adicionar("Frango de Corte", "Pre-Inicial", proteinaBruta, "24.27");
        adicionar("Frango de Corte", "Pre-Inicial", energiaMetabolizavel, "2975");

        adicionar("Frango de Corte", "Inicial", proteinaBruta, "23.31");
        adicionar("Frango de Corte", "Inicial", energiaMetabolizavel, "3050");

        adicionar("Frango de Corte", "Crescimento I", proteinaBruta, "20.58");
        adicionar("Frango de Corte", "Crescimento I", energiaMetabolizavel, "3150");

        adicionar("Frango de Corte", "Crescimento II", proteinaBruta, "18.57");
        adicionar("Frango de Corte", "Crescimento II", energiaMetabolizavel, "3200");

        adicionar("Frango de Corte", "Final", proteinaBruta, "17.47");
        adicionar("Frango de Corte", "Final", energiaMetabolizavel, "3250");
    }

    private void adicionar(String tipoAve, String fase, Nutriente nutriente, String minimo) {
        ExigenciaNutricional exigencia = new ExigenciaNutricional(
                null,
                tipoAve,
                fase,
                nutriente,
                new BigDecimal(minimo),
                null
        );
        exigencias.add(exigencia);
    }

    private boolean textoIgual(String texto1, String texto2) {
        if (texto1 == null || texto2 == null) {
            return false;
        }

        return texto1.trim().equalsIgnoreCase(texto2.trim());
    }

    private boolean mesmoNutriente(Nutriente nutriente1, Nutriente nutriente2) {
        if (nutriente1 == null || nutriente2 == null) {
            return false;
        }

        if (nutriente1 == nutriente2) {
            return true;
        }

        if (nutriente1.getId() != null && nutriente2.getId() != null
                && nutriente1.getId().equals(nutriente2.getId())) {
            return true;
        }

        if (nutriente1.getAbreviacao() != null && nutriente2.getAbreviacao() != null
                && nutriente1.getAbreviacao().equalsIgnoreCase(nutriente2.getAbreviacao())) {
            return true;
        }

        return false;
    }
}
