package com.example.backend.dto;

import com.example.backend.model.StatusTarefa; // Import para StatusTarefa
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List; // Import para List

@Data
public class TarefaRequest {
    private String titulo;
    private String descricao;
    private StatusTarefa status; // Pode ser PENDENTE, EM_ANDAMENTO, CONCLUIDA
    private LocalDateTime dataLimite;
    private Long atribuidorId; // ID do usuário que atribui a tarefa
    private Long atributarioId; // ID do usuário para quem a tarefa foi atribuída

    // NOVOS CAMPOS PARA ATRIBUIR INSUMOS E MAQUINÁRIOS
    private List<TarefaInsumoRequest> insumosAtribuidos;
    private List<TarefaMaquinarioRequest> maquinariosAtribuidos;
}
