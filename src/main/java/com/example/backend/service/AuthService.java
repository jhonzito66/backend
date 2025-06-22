package com.example.backend.service;

import com.example.backend.dto.LoginRequest;
import com.example.backend.dto.LoginResponse;
import com.example.backend.dto.RegistroRequest;
import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registrarUsuario(RegistroRequest request) {
        // Verifica se o email já está em uso.
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return false; // Email já existe
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        // Codifica a senha antes de salvar no banco de dados.
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTipoUsuario(Usuario.TipoUsuario.valueOf(request.getTipoUsuario().toUpperCase()));

        usuarioRepository.save(usuario);
        return true;
    }

    public LoginResponse autenticarUsuario(LoginRequest request) {
        // Busca o usuário pelo email.
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(request.getEmail());

        if (optionalUsuario.isEmpty()) {
            // Retorna LoginResponse com ID nulo para usuário não encontrado
            return new LoginResponse(false, null, "Usuário não encontrado.", null);
        }

        Usuario usuario = optionalUsuario.get();
        // Compara a senha fornecida com a senha codificada no banco de dados.
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            // Retorna LoginResponse com ID nulo para senha incorreta
            return new LoginResponse(false, null, "Senha incorreta.", null);
        }

        // Retorna sucesso, o tipo de usuário e o ID do usuário.
        return new LoginResponse(true, usuario.getTipoUsuario().name(), "Login bem-sucedido!", usuario.getId());
    }
}
