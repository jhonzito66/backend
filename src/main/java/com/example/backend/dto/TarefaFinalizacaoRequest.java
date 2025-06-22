package com.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

// DTO usado para finalizar uma tarefa, incluindo observações, insumos e maquinários envolvidos
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaFinalizacaoRequest {

    private String observacao; // Observação final do usuário sobre a tarefa

    private List<InsumoEntregueRequest> insumosEntregues; // Lista de insumos entregues na tarefa

    private List<MaquinarioDevolvidoRequest> maquinariosDevolvidos; // Lista de maquinários devolvidos na tarefa

    // Subclasse que representa um insumo entregue
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsumoEntregueRequest {
        private Long tarefaInsumoId;      // ID do vínculo entre tarefa e insumo
        private Boolean entregue;         // Indica se o insumo foi realmente entregue
        private LocalDateTime dataEntrega; // Data/hora da entrega
    }

    // Subclasse que representa um maquinário devolvido
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaquinarioDevolvidoRequest {
        private Long tarefaMaquinarioId;   // ID do vínculo entre tarefa e maquinário
        private Boolean foiLavado;         // Indica se o maquinário foi lavado
        private Boolean foiAbastecido;     // Indica se o maquinário foi abastecido
        private LocalDateTime dataDevolucao; // Data/hora da devolução
    }
}
