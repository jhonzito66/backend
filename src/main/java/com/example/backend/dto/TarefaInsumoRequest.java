package com.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO para requisição de TarefaInsumo (usado para atribuir insumos a uma tarefa)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaInsumoRequest {
    private Long insumoId;
    private BigDecimal quantidade; // MUDANÇA: BigDecimal
}