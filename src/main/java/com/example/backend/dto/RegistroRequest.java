package com.example.backend.dto;

import lombok.Data;

// DTO usado para registrar um novo usuário
@Data // Gera getters, setters, toString, equal e hashCode
public class RegistroRequest {

    private String nome;            // Nome completo do usuário
    private String email;           // Email do usuário (usado para login)
    private String senha;           // Senha para autenticação
    private String tipoUsuario;     // Tipo de usuário: "admin", "comum" ou "moderador"
}
