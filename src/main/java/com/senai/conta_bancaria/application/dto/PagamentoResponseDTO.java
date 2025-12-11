package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.enums.StatusPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoResponseDTO(
        @NotBlank(message = "O número da conta é obrigatório")
        String numeroConta,

        @NotBlank(message = "O boleto é obrigatório")
        String boleto,

        @NotNull(message = "O campo 'Valor Pago' é obrigatório.")
        @DecimalMin(value = "0.00", message = "O vaor pago deve ser maior ou igual a zero")
        BigDecimal valorPago,

        LocalDateTime dataPagamento,

        @NotNull(message = "O status não pode estar vazio")
        StatusPagamento status,

        @NotNull(message = "A lista de taxas não pode ser nula")
        List<TaxaResponseDTO> taxas
) {
    public static PagamentoResponseDTO fromEntity(Pagamento pagamento) {
        List<TaxaResponseDTO> taxas = pagamento
                .getTaxas()
                .stream()
                .map(TaxaResponseDTO::fromEntity)
                .toList();

        return new PagamentoResponseDTO(
                pagamento.getConta().getNumero(),
                pagamento.getBoleto(),
                pagamento.getValorPago(),
                pagamento.getDataPagamento(),
                pagamento.getStatus(),
                taxas
        );
    }
}