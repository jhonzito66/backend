package com.example.backend.dto;

import com.example.backend.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

// DTO para retorno básico de informações do usuário
@Data // Gera getters, setters, equals, hashCode e toString
@AllArgsConstructor // Construtor com todos os campos
public class UsuarioResponseDTO {
    private Long id;                      // ID do usuário
    private String nome;                  // Nome do usuário
    private String email;                 // Email do usuário
    private Usuario.TipoUsuario tipoUsuario; // Tipo do usuário (ADMIN, COMUM, MODERADOR)
}
