package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Taxa;

import java.math.BigDecimal;

public record TaxaRegistroDTO(
        String descricao,
        BigDecimal percentual
) {
    public static TaxaRegistroDTO fromEntity(Taxa taxa){
        return new TaxaRegistroDTO(
                taxa.getDescricao(),
                taxa.getPercentual()
        );
    }
}