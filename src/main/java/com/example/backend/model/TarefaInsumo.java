package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Para valores precisos (quantidade)
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefa_insumo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementado
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Relaciona com a tarefa
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @ManyToOne(fetch = FetchType.LAZY) // Relaciona com o insumo
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(nullable = false)
    private BigDecimal quantidade; // Quantidade do insumo para a tarefa

    @Column(name = "entregue", nullable = false)
    private Boolean entregue = false; // Se o insumo foi entregue

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega; // Data em que o insumo foi entregue

    // Garante que 'entregue' nunca fique nulo
    @PrePersist
    @PreUpdate
    private void setDefaultValues() {
        if (this.entregue == null) {
            this.entregue = false;
        }
    }
}
