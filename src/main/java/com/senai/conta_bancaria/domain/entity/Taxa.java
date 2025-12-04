package com.senai.conta_bancaria.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "taxa",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id")
        }
)
public class Taxa {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 120)
    private String descricao;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal percentual;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorFixo;
}