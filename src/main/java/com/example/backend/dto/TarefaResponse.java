package com.example.backend.dto;

import com.example.backend.model.StatusTarefa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// DTO para resposta de tarefas (dados enviados ao frontend)
@Data // Gera getters, setters, equals, hashCode e toString
@NoArgsConstructor // Construtor vazio
@AllArgsConstructor // Construtor com todos os campos
public class TarefaResponse {

    private Long id;                       // ID da tarefa
    private String titulo;                 // Título da tarefa
    private String descricao;              // Descrição da tarefa
    private StatusTarefa status;           // Status da tarefa (PENDENTE, EM_ANDAMENTO, CONCLUIDA)
    private LocalDateTime dataCriacao;     // Data de criação da tarefa
    private LocalDateTime dataLimite;      // Data limite para conclusão
    private Long atribuidorId;             // ID do usuário que atribuiu a tarefa
    private String atribuidorNome;         // Nome do usuário que atribuiu a tarefa
    private Long atributarioId;            // ID do usuário que recebeu a tarefa
    private String atributarioNome;        // Nome do usuário que recebeu a tarefa
    private String observacaoFinalizacao; // Observações feitas na finalização da tarefa
    private LocalDateTime dataConclusao;   // Data em que a tarefa foi concluída

    // Listas de insumos e maquinários associados à tarefa
    private List<TarefaInsumoResponse> insumosAtribuidos;         // Insumos usados na tarefa
    private List<TarefaMaquinarioResponse> maquinariosAtribuidos; // Maquinários usados na tarefa
}
