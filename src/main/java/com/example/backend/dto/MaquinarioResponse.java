package com.example.backend.dto;

import com.example.backend.model.MaquinarioStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO usado para retornar dados de um maquinário ao cliente
@Data // Gera getters, setters, toString, equal e hashCode
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os campos
public class MaquinarioResponse {

    private Long id;                      // ID do maquinário
    private String nome;                  // Nome do maquinário
    private String descricao;             // Descrição detalhada
    private MaquinarioStatus status;      // Status atual: DISPONIVEL, EM_USO ou MANUTENCAO
    private Boolean lavado;               // Se o maquinário está lavado
    private Boolean abastecido;           // Se o maquinário está abastecido
}
