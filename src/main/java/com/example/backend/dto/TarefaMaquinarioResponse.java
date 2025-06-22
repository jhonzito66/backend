package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarefaMaquinarioResponse {
    private Long id;
    private Long maquinarioId;
    private String maquinarioNome; // Nome do maquinário
    private String maquinarioStatusAtual; // Status atual do maquinário (DISPONIVEL, EM_USO, etc.)
    private Integer quantidade;
    private LocalDateTime dataDevolucao;
    private Boolean foiLavado;
    private Boolean foiAbastecido;
}
