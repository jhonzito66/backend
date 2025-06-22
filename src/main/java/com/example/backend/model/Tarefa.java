package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tarefas")
@Data // Gera getters, setters, toString, equals e hashCode automaticamente
@NoArgsConstructor // Construtor padrão sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementado no banco
    private Long id;

    @Column(nullable = false)
    private String titulo; // Título da tarefa

    @Column(columnDefinition = "TEXT") // Texto longo para descrição
    private String descricao; // Descrição da tarefa

    @Enumerated(EnumType.STRING) // Salva enum como String no banco
    @Column(nullable = false, name = "status")
    private StatusTarefa status; // Status da tarefa (PENDENTE, EM_ANDAMENTO, CONCLUIDA)

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao; // Data de criação da tarefa

    @Column(name = "data_limite")
    private LocalDateTime dataLimite; // Data limite para conclusão

    // Usuário que atribuiu a tarefa
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atribuidor_id", nullable = false)
    private Usuario atribuidor;

    // Usuário para quem a tarefa foi atribuída
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atributario_id", nullable = false)
    private Usuario atributario;

    @Column(name = "observacao_finalizacao", columnDefinition = "TEXT")
    private String observacaoFinalizacao; // Observação ao finalizar a tarefa

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao; // Data em que a tarefa foi concluída

    // Lista de insumos atribuídos à tarefa
    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TarefaInsumo> insumosAtribuidos;

    // Lista de maquinários atribuídos à tarefa
    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TarefaMaquinario> maquinariosAtribuidos;

    // Define data de criação e status padrão antes de salvar no banco
    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (status == null) {
            status = StatusTarefa.PENDENTE;
        }
    }
}
