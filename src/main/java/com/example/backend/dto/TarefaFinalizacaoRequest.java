package com.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List; // Import para List

// DTO para a requisição de finalização de tarefa, incluindo detalhes de insumo/maquinário.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaFinalizacaoRequest {
    private String observacao;
    private List<InsumoEntregueRequest> insumosEntregues; // Lista de insumos entregues
    private List<MaquinarioDevolvidoRequest> maquinariosDevolvidos; // Lista de maquinários devolvidos

    // DTO aninhado para insumos entregues
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsumoEntregueRequest {
        private Long tarefaInsumoId; // ID da entrada em tarefa_insumo
        private Boolean entregue;
        private LocalDateTime dataEntrega;
    }

    // DTO aninhado para maquinários devolvidos
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaquinarioDevolvidoRequest {
        private Long tarefaMaquinarioId; // ID da entrada em tarefa_maquinario
        private Boolean foiLavado;
        private Boolean foiAbastecido;
        private LocalDateTime dataDevolucao;
    }
}
