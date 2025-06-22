package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Gera getters, setters, toString, equal e hashCode
@AllArgsConstructor // Gera construtor com todos os campos
@NoArgsConstructor  // Gera construtor vazio (necessário para alguns frameworks)
public class LoginResponse {

    private boolean success;        // Indica se o login foi bem-sucedido
    private String tipoUsuario;     // Tipo de usuário: ADMIN, COMUM ou MODERADOR
    private String message;         // Mensagem de retorno (ex: "Login realizado com sucesso")
    private Long id;                // ID do usuário autenticado
}
