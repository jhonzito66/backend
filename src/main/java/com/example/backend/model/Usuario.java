package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "usuarios")
@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Construtor padrão (sem argumentos)
@AllArgsConstructor // Construtor com todos os argumentos
// Implementa UserDetails para integração com Spring Security
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementado
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome; // Nome do usuário

    @Column(nullable = false, unique = true, length = 100)
    private String email; // Email usado como username

    @Column(nullable = false, length = 100)
    private String senha; // Senha criptografada

    @Column(name = "tipo_usuario", nullable = false, length = 20)
    @Enumerated(EnumType.STRING) // Armazena enum como string no banco
    private TipoUsuario tipoUsuario;

    // Enum para tipos de usuário
    public enum TipoUsuario {
        ADMIN, COMUM, MODERADOR
    }

    // --- Métodos obrigatórios da interface UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna a lista de permissões/roles do usuário
        return Collections.singletonList(new SimpleGrantedAuthority(tipoUsuario.name()));
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna a senha (criptografada)
    }

    @Override
    public String getUsername() {
        return this.email; // Usa email como nome de usuário
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta nunca expira (exemplo simples)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Conta nunca é bloqueada (exemplo simples)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciais nunca expiram (exemplo simples)
    }

    @Override
    public boolean isEnabled() {
        return true; // Usuário sempre habilitado (exemplo simples)
    }
}
