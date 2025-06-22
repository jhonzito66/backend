package com.example.backend.dto;

import com.example.backend.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;

// DTO simples para retornar informações básicas do usuário ao frontend,
// especialmente para a lista de usuários atribuíveis.
@Data
@AllArgsConstructor
public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Usuario.TipoUsuario tipoUsuario;
}
