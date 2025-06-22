package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Import para BigDecimal
import java.time.LocalDate;

@Entity
@Table(name = "insumos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "unidade_medida", nullable = false)
    private String unidadeMedida; // Ex: "KG", "LITRO", "UNIDADE"

    @Column(nullable = false)
    private BigDecimal quantidade; // MUDANÇA: Usamos BigDecimal para NUMERIC

    @Column(name = "data_validade")
    private LocalDate dataValidade; // Date no SQL mapeia para LocalDate

    // Relacionamento OneToMany com TarefaInsumo
    // FetchType.LAZY para evitar carregamento ansioso desnecessário
    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<TarefaInsumo> tarefasInsumos;
}
