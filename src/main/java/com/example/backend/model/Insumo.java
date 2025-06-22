package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "insumos")
@Data // Gera getters, setters, equals, hashCode e toString
@NoArgsConstructor // Construtor vazio
@AllArgsConstructor // Construtor com todos os campos
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // Identificador único do insumo

    @Column(nullable = false)
    private String nome;                // Nome do insumo

    @Column(columnDefinition = "TEXT")
    private String descricao;           // Descrição detalhada do insumo

    @Column(name = "unidade_medida", nullable = false)
    private String unidadeMedida;       // Unidade de medida (ex: KG, LITRO, UNIDADE)

    @Column(nullable = false)
    private BigDecimal quantidade;      // Quantidade disponível do insumo

    @Column(name = "data_validade")
    private LocalDate dataValidade;     // Data de validade do insumo

    // Relação com tarefas que utilizam este insumo
    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<TarefaInsumo> tarefasInsumos; // Lista de tarefas que usam este insumo
}
