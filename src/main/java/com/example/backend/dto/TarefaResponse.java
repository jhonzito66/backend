package com.example.backend.dto;

import com.example.backend.model.StatusTarefa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // Adicionado construtor padrão para flexibilidade

import java.time.LocalDateTime;
import java.util.List; // Import para List

// DTO para respostas de tarefas (retorno de informações ao frontend).
@Data
@NoArgsConstructor
@AllArgsConstructor // Este construtor pode se tornar muito longo, mas o Lombok Data resolve Getters/Setters/ToString
public class TarefaResponse {
    private Long id;
    private String titulo;
    private String descricao;
    private StatusTarefa status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataLimite;
    private Long atribuidorId;
    private String atribuidorNome; // Nome do usuário que atribuiu
    private Long atributarioId;
    private String atributarioNome; // Nome do usuário para quem foi atribuída
    private String observacaoFinalizacao; // Observação de finalização
    private LocalDateTime dataConclusao; // Data de conclusão da tarefa

    // NOVOS CAMPOS PARA INSUMOS E MAQUINÁRIOS ATRIBUÍDOS
    private List<TarefaInsumoResponse> insumosAtribuidos;
    private List<TarefaMaquinarioResponse> maquinariosAtribuidos;
}
