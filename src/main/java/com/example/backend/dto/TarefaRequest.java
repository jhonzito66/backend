package com.example.backend.dto;

import com.example.backend.model.StatusTarefa; // Import para StatusTarefa
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List; // Import para List

// DTO usado para criar ou atualizar uma tarefa
@Data // Gera getters, setters, equals, hashCode e toString
public class TarefaRequest {

    private String titulo;                       // Título da tarefa
    private String descricao;                    // Descrição detalhada da tarefa
    private StatusTarefa status;                 // Status da tarefa (PENDENTE, EM_ANDAMENTO, CONCLUIDA)
    private LocalDateTime dataLimite;            // Data limite para conclusão da tarefa
    private Long atribuidorId;                   // ID do usuário que atribui a tarefa
    private Long atributarioId;                  // ID do usuário que receberá a tarefa

    // Listas de insumos e maquinários atribuídos à tarefa
    private List<TarefaInsumoRequest> insumosAtribuidos;         // Insumos associados à tarefa
    private List<TarefaMaquinarioRequest> maquinariosAtribuidos; // Maquinários associados à tarefa
}
