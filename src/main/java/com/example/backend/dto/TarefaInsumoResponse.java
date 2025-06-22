package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaInsumoResponse {
    private Long id;
    private Long insumoId;
    private String insumoNome; // Nome do insumo
    private String unidadeMedida;
    private BigDecimal quantidade; // Quantidade atribuída à tarefa (MUDANÇA: BigDecimal)
    private Boolean entregue;
    private LocalDateTime dataEntrega;
}