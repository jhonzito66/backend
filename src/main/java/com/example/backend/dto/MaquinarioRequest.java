package com.example.backend.dto;

import com.example.backend.model.MaquinarioStatus; // Import do enum de status
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO para requisição de Maquinario
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaquinarioRequest {
    private String nome;
    private String descricao;
    private MaquinarioStatus status; // DISPONIVEL, EM_USO, MANUTENCAO
    private Boolean lavado;
    private Boolean abastecido;
}