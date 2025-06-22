package com.example.backend.dto;

import com.example.backend.model.MaquinarioStatus; // Enum com os status possíveis
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO usado para criar ou atualizar maquinários
@Data // Gera getters, setters, toString, equal e hashCode
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os campos
public class MaquinarioRequest {

    private String nome;                // Nome do maquinário
    private String descricao;          // Descrição do maquinário
    private MaquinarioStatus status;   // Status: DISPONIVEL, EM_USO ou MANUTENCAO
    private Boolean lavado;            // Indica se está lavado
    private Boolean abastecido;        // Indica se está abastecido
}
