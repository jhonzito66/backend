package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal; // Usar BigDecimal para 'numeric' em casos de precisão financeira
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefa_insumo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento ManyToOne com Tarefa
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @ManyToOne(fetch = FetchType.LAZY) // Relacionamento ManyToOne com Insumo
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(nullable = false)
    private BigDecimal quantidade; // Quantidade de insumo para esta tarefa

    // Novos campos para a finalização por parte do usuário comum
    @Column(name = "entregue", nullable = false) // Indica se o insumo foi entregue
    private Boolean entregue = false;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega; // Data da entrega do insumo

    @PrePersist
    @PreUpdate
    private void setDefaultValues() {
        if (this.entregue == null) {
            this.entregue = false;
        }
    }
}
