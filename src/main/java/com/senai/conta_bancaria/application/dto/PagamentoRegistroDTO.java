package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Pagamento;
import com.senai.conta_bancaria.domain.entity.Taxa;
import com.senai.conta_bancaria.domain.enums.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record PagamentoRegistroDTO(
        String boleto,
        BigDecimal valorPago,
        StatusPagamento status,
        List<TaxaRegistroDTO> taxas
) {
    public Pagamento toEntity(Conta conta) {
        return Pagamento.builder()
                .conta(conta)
                .servico(boleto)
                .valorPago(valorPago)
                .dataPagamento(LocalDateTime.now())
                .status(StatusPagamento.PROCESSANDO)
                .taxas(new ArrayList<>())
                .build();
    }
}