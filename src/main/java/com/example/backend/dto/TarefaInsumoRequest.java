package com.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

// DTO usado para associar um insumo a uma tarefa
@Data // Gera getters, setters, equals, hashCode e toString
@NoArgsConstructor // Construtor vazio
@AllArgsConstructor // Construtor com todos os campos
public class TarefaInsumoRequest {

    private Long insumoId;           // ID do insumo a ser associado à tarefa
    private BigDecimal quantidade;  // Quantidade do insumo (permite valores decimais)
}
