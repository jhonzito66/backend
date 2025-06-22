package com.example.backend.dto;

import lombok.Data;

@Data
public class RegistroRequest {
    private String nome;
    private String email;
    private String senha;
    private String tipoUsuario; // "admin", "comum", "moderador"
}