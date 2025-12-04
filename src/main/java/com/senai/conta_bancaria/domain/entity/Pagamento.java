package com.senai.conta_bancaria.domain.entity;

import com.senai.conta_bancaria.domain.enums.StatusPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "pagamento",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        }
)
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_id", foreignKey = @ForeignKey(name = "fk_pagamento_conta"))
    private Conta conta;

    @Column(nullable = false, length = 120)
    private String boleto;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPago;

    @Column(nullable = false)
    private LocalDateTime dataPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected StatusPagamento status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "taxa_pagamento",
            joinColumns = @JoinColumn(name = "pagamento_id"),
            inverseJoinColumns = @JoinColumn(name = "taxa_id"))
    private List<Taxa> taxas;
}