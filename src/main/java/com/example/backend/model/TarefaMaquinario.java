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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementado
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Relaciona com a tarefa
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @ManyToOne(fetch = FetchType.LAZY) // Relaciona com o maquinário
    @JoinColumn(name = "maquinario_id", nullable = false)
    private Maquinario maquinario;

    @Column
    private Integer quantidade; // Quantidade do maquinário (geralmente 1)

    @Column(name = "data_devolucao")
    private LocalDateTime dataDevolucao; // Quando foi devolvido

    @Column(name = "foi_lavado", nullable = false)
    private Boolean foiLavado = false; // Se foi lavado

    @Column(name = "foi_abastecido", nullable = false)
    private Boolean foiAbastecido = false; // Se foi abastecido

    // Garante que os booleanos não fiquem nulos
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
