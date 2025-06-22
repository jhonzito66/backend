package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List; // Import para List

@Entity
@Table(name = "tarefas")
@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT") // Define o tipo de coluna para texto longo
    private String descricao;

    @Enumerated(EnumType.STRING) // Armazena o enum como String (PENDENTE, EM_ANDAMENTO, CONCLUIDA)
    @Column(nullable = false, name = "status")
    private StatusTarefa status;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_limite")
    private LocalDateTime dataLimite;

    // Relacionamento com o usuário que atribuiu a tarefa
    @ManyToOne(fetch = FetchType.LAZY) // Eager loading can cause performance issues
    @JoinColumn(name = "atribuidor_id", nullable = false)
    private Usuario atribuidor;

    // Relacionamento com o usuário para quem a tarefa foi atribuída
    @ManyToOne(fetch = FetchType.LAZY) // Eager loading can cause performance issues
    @JoinColumn(name = "atributario_id", nullable = false)
    private Usuario atributario;

    // Campos de finalização
    @Column(name = "observacao_finalizacao", columnDefinition = "TEXT")
    private String observacaoFinalizacao;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    // NOVOS RELACIONAMENTOS COM INSUMOS E MAQUINÁRIOS PARA ESTA TAREFA
    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TarefaInsumo> insumosAtribuidos;

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TarefaMaquinario> maquinariosAtribuidos;


    // Método pré-persist para definir data_criacao automaticamente
    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (status == null) {
            status = StatusTarefa.PENDENTE; // Define PENDENTE como status padrão
        }
    }
}
