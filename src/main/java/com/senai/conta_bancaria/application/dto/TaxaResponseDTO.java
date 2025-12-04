package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Taxa;

import java.math.BigDecimal;

public record TaxaResponseDTO(
        String descricao,
        BigDecimal percentual,
        BigDecimal valorFixo
) {
    public static TaxaResponseDTO fromEntity(Taxa taxa){
        return new TaxaResponseDTO(
                taxa.getDescricao(),
                taxa.getPercentual(),
                taxa.getValorFixo()
        );
    }
}