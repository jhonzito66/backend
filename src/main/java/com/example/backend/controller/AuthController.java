package com.example.backend.controller;


import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.RegistroRequest;
import com.example.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistroRequest request) {
        // Tenta registrar o usuário.
        if (authService.registrarUsuario(request)) {
            return ResponseEntity.ok("Usuário registrado com sucesso!");
        } else {
            return ResponseEntity.badRequest().body("Email já em uso.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Tenta autenticar o usuário.
        LoginResponse response = authService.autenticarUsuario(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response); // 401 Unauthorized
        }
    }
}
