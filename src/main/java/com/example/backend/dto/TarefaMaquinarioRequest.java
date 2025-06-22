package com.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

// DTO para requisição de TarefaMaquinario (usado para atribuir maquinários a uma tarefa)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaMaquinarioRequest {
    private Long maquinarioId;
    private Integer quantidade; // Se houver múltiplos do mesmo maquinário, geralmente 1 para item único
}