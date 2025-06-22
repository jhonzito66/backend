package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections; // Import para Collections

@Entity
@Table(name = "usuarios")
@Data // Gera getters, setters, toString, equals e hashCode
@NoArgsConstructor // Construtor sem argumentos
@AllArgsConstructor // Construtor com todos os argumentos
// Implementa UserDetails para ser usado pelo Spring Security
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incremento para PostgreSQL
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100) // A senha será armazenada criptografada
    private String senha;

    @Column(name = "tipo_usuario", nullable = false, length = 20)
    @Enumerated(EnumType.STRING) // Armazena o enum como String
    private TipoUsuario tipoUsuario;

    public enum TipoUsuario {
        ADMIN, COMUM, MODERADOR
    }

    // --- Implementação da interface UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Retorna as permissões/roles do usuário.
        // Convertemos o nosso TipoUsuario para uma SimpleGrantedAuthority.
        return Collections.singletonList(new SimpleGrantedAuthority(tipoUsuario.name()));
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna a senha criptografada
    }

    @Override
    public String getUsername() {
        return this.email; // O email será o username para autenticação
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta nunca expira para este exemplo
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Conta nunca é bloqueada para este exemplo
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciais nunca expiram para este exemplo
    }

    @Override
    public boolean isEnabled() {
        return true; // Usuário sempre habilitado para este exemplo
    }
}
