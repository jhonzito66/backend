package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsumoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private String unidadeMedida;
    private BigDecimal quantidade;
    private LocalDate dataValidade;
}