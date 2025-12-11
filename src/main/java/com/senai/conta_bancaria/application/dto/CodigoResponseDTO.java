package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.CodigoAutenticacao;

import java.time.LocalDateTime;

public record CodigoResponseDTO(
        String id,
        String codigo,
        LocalDateTime expiraEm,
        boolean validado,
        String cliente
) {
    public static CodigoResponseDTO fromEntity(CodigoAutenticacao codigoAutenticacao) {
        return new CodigoResponseDTO(
                codigoAutenticacao.getId(),
                codigoAutenticacao.getCodigo(),
                codigoAutenticacao.getExpiraEm(),
                codigoAutenticacao.isValidado(),
                codigoAutenticacao.getCliente().getCpf()
        );
    }
}