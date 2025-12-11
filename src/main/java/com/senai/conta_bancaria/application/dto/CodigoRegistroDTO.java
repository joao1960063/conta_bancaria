package com.senai.conta_bancaria.application.dto;

import java.time.LocalDateTime;

public record CodigoRegistroDTO(
        String codigo,
        LocalDateTime expiraEm,
        String cliente
) {
}