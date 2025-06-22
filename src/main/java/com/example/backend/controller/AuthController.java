package com.example.backend.controller;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.RegistroRequest;
import com.example.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pelos endpoints de autenticação, como registro e login de usuários.
 * Todas as rotas aqui definidas estão sob o prefixo /api/auth.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Construtor para injeção de dependência do AuthService.
     * A anotação @Autowired é opcional em construtores a partir do Spring 4.3.
     *
     * @param authService O serviço que encapsula a lógica de negócio de autenticação.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint para registrar um novo usuário no sistema.
     *
     * @param request O DTO (Data Transfer Object) contendo os dados para o registro.
     * @return ResponseEntity com status 201 (Created) em caso de sucesso,
     *         ou 400 (Bad Request) se o email já estiver em uso.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistroRequest request) {
        boolean sucesso = authService.registrarUsuario(request);
        if (sucesso) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Email já está em uso.");
        }
    }

    /**
     * Endpoint para autenticar um usuário existente e gerar uma resposta de login.
     *
     * @param request O DTO contendo as credenciais (email e senha) para o login.
     * @return ResponseEntity com o LoginResponse (contendo token, etc.) e status 200 (OK),
     *         ou uma resposta com status 401 (Unauthorized) em caso de falha na autenticação.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.autenticarUsuario(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}