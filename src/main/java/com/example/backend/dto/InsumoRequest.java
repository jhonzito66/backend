package com.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO para requisição de Insumo
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsumoRequest {
    private String nome;
    private String descricao;
    private String unidadeMedida;
    private BigDecimal quantidade;
    private LocalDate dataValidade;
}