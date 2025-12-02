package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferenciaDTO(
        @NotBlank(message = "Conta de origem é obrigatória")
        String contaDestino,

        @NotNull(message = "Valor é obrigatório")
        BigDecimal valor
) {

}
