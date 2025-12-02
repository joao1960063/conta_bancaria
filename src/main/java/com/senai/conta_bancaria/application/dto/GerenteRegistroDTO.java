package com.senai.conta_bancaria.application.dto;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.entity.Conta;
import com.senai.conta_bancaria.domain.entity.Gerente;
import com.senai.conta_bancaria.domain.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;

public record GerenteRegistroDTO(
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
        String senha,

        @Valid
        @NotNull(message = "A conta não pode ser nula")
        ContaResumoDTO contaDTO
) {
    public Gerente toEntity() {
        return Gerente.builder()
                .ativo(true)
                .nome(this.nome)
                .cpf(this.cpf)
                .email(this.email)
                .senha(this.senha)
                .role(Role.GERENTE)
                .build();
    }
}
