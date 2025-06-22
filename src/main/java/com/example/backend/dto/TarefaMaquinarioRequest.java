package com.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

// DTO usado para associar um maquinário a uma tarefa
@Data // Gera getters, setters, equals, hashCode e toString
@NoArgsConstructor // Construtor vazio
@AllArgsConstructor // Construtor com todos os campos
public class TarefaMaquinarioRequest {

    private Long maquinarioId;    // ID do maquinário a ser associado à tarefa
    private Integer quantidade;   // Quantidade do maquinário (geralmente 1 para itens únicos)
}
