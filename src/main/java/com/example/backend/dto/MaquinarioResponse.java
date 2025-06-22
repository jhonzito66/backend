package com.example.backend.dto;

import com.example.backend.model.MaquinarioStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaquinarioResponse {
    private Long id;
    private String nome;
    private String descricao;
    private MaquinarioStatus status;
    private Boolean lavado;
    private Boolean abastecido;
}
