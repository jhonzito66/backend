package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarefa_maquinario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaMaquinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento ManyToOne com Tarefa
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento ManyToOne com Maquinario
    @JoinColumn(name = "maquinario_id", nullable = false)
    private Maquinario maquinario;

    @Column
    private Integer quantidade; // Quantidade de maquinário (se aplicável, ou 1 para item único)

    // Novos campos para a finalização por parte do usuário comum
    @Column(name = "data_devolucao") // Data de devolução do maquinário
    private LocalDateTime dataDevolucao;

    @Column(name = "foi_lavado", nullable = false) // Se o maquinário foi lavado
    private Boolean foiLavado = false;

    @Column(name = "foi_abastecido", nullable = false) // Se o maquinário foi abastecido
    private Boolean foiAbastecido = false;

    @PrePersist
    @PreUpdate
    private void setDefaultValues() {
        if (this.foiLavado == null) {
            this.foiLavado = false;
        }
        if (this.foiAbastecido == null) {
            this.foiAbastecido = false;
        }
    }
}
