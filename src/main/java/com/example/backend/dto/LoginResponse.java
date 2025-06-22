package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // Adicionado para construtor padrão, se necessário por outras libs

@Data
@AllArgsConstructor
@NoArgsConstructor // Adicione este construtor sem argumentos também para flexibilidade
public class LoginResponse {
    private boolean success;
    private String tipoUsuario; // Pode ser "ADMIN", "COMUM", "MODERADOR" ou null
    private String message;
    private Long id; // NOVO: ID do usuário logado
}
