package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ContaAtualizacaoDTO(
        @NotNull(message = "O campo 'saldo' é obrigatório.")
        @DecimalMin(value = "0.00", message = "O saldo deve ser maior ou igual a zero")
        BigDecimal saldo,

        @NotNull(message = "O campo 'limite' é obrigatório.")
        @DecimalMin(value = "0.00", message = "O limite deve ser maior ou igual a zero.")
        BigDecimal limite,

        @NotNull(message = "O campo 'rendimento' é obrigatório.")
        @DecimalMin(value = "0.00", message = "O rendimento deve ser maior que zero.")
        BigDecimal rendimento,

        @NotNull(message = "O campo 'taxa' é obrigatório.")
        @DecimalMin(value = "0.00", message = "A taxa deve ser maior que zero.")
        BigDecimal taxa
) {
}
