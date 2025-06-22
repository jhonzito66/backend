package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// DTO para retornar dados de maquinário associado a uma tarefa
@Data // Gera getters, setters, equals, hashCode e toString
@NoArgsConstructor // Construtor vazio
@AllArgsConstructor // Construtor com todos os campos
public class TarefaMaquinarioResponse {

    private Long id;                      // ID do vínculo tarefa-maquinário
    private Long maquinarioId;            // ID do maquinário
    private String maquinarioNome;        // Nome do maquinário
    private String maquinarioStatusAtual; // Status atual do maquinário (ex: DISPONIVEL, EM_USO)
    private Integer quantidade;           // Quantidade de maquinários usados
    private LocalDateTime dataDevolucao;  // Data/hora da devolução do maquinário
    private Boolean foiLavado;            // Se o maquinário foi lavado ao ser devolvido
    private Boolean foiAbastecido;        // Se o maquinário foi abastecido ao ser devolvido
}
