package com.senai.conta_bancaria.domain.entity;

import com.senai.conta_bancaria.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected String id;

    @NotBlank
    @Column(nullable = false, length = 120)
    protected String nome;

    @NotBlank
    @Column(nullable = false, unique = true, length = 11)
    protected String cpf;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    protected String email;

    @NotBlank
    @Column(nullable = false)
    protected String senha;

    @Column(nullable = false)
    protected Boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;
}
