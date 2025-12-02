package com.senai.conta_bancaria.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record ClienteAtualizadoDTO(
        @NotBlank(message = "O nome não pode estar vazio")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$",
                message = "O nome deve conter apenas letras")
        String nome,

        @NotBlank(message = "O CPF não pode estar vazio")
        @Pattern(regexp = "\\d{11}",
                message = "O CPF deve conter 11 dígitos numéricos")
        String cpf,

        @NotBlank(message = "O email não pode estar vazio")
        @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$",
                message = "O email deve ser válido"
        )
        String email,

        @NotBlank(message = "A senha não pode estar vazia")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, incluindo letra maiúscula, minúscula, número e caractere especial"
        )
        String senha
) {

}
