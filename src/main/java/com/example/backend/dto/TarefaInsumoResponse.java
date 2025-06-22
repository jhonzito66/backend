package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// DTO usado para retornar os dados de um insumo associado a uma tarefa
@Data // Gera getters, setters, equals, hashCode e toString
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os campos
public class TarefaInsumoResponse {

    private Long id;                      // ID do vínculo tarefa-insumo
    private Long insumoId;                // ID do insumo
    private String insumoNome;            // Nome do insumo
    private String unidadeMedida;         // Unidade de medida do insumo (ex: kg, L)
    private BigDecimal quantidade;        // Quantidade de insumo atribuída à tarefa
    private Boolean entregue;             // Indica se o insumo foi entregue
    private LocalDateTime dataEntrega;    // Data/hora da entrega do insumo
}
