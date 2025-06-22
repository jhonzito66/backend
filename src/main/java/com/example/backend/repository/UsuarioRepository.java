package com.example.backend.repository;

import com.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Encontra um usuário pelo email.
    Optional<Usuario> findByEmail(String email);

    // Verifica se um email já existe.
    boolean existsByEmail(String email);

    // Novo método para encontrar usuários por tipo (para listar comuns para atribuição)
    List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    // NOVO MÉTODO: Para encontrar usuários por uma lista de tipos (COMUM e MODERADOR)
    List<Usuario> findByTipoUsuarioIn(List<Usuario.TipoUsuario> tiposUsuario);
}