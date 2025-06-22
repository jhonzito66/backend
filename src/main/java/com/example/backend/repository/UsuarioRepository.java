package com.example.backend.repository;

import com.example.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// Repositório para gerenciar a entidade Usuario no banco de dados
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca um usuário pelo email (retorna Optional porque pode não existir)
    Optional<Usuario> findByEmail(String email);

    // Verifica se já existe um usuário com o email informado
    boolean existsByEmail(String email);

    // Busca usuários por tipo (exemplo: ADMIN, COMUM, MODERADOR)
    List<Usuario> findByTipoUsuario(Usuario.TipoUsuario tipoUsuario);

    // Busca usuários cujo tipo esteja em uma lista (exemplo: COMUM e MODERADOR)
    List<Usuario> findByTipoUsuarioIn(List<Usuario.TipoUsuario> tiposUsuario);
}
