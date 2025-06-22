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

    // Método para registrar um novo usuário
    public boolean registrarUsuario(RegistroRequest request) {
        // Verifica se o email já está cadastrado no sistema
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return false; // Email já existe, não registra
        }

        // Cria um novo objeto Usuario com os dados do request
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        // Codifica a senha para segurança antes de salvar
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        // Define o tipo de usuário convertendo para enum maiúsculo
        usuario.setTipoUsuario(Usuario.TipoUsuario.valueOf(request.getTipoUsuario().toUpperCase()));

        // Salva o usuário no banco de dados
        usuarioRepository.save(usuario);
        return true; // Registro realizado com sucesso
    }

    // Método para autenticar usuário durante login
    public LoginResponse autenticarUsuario(LoginRequest request) {
        // Busca o usuário pelo email informado
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(request.getEmail());

        // Se usuário não encontrado, retorna falha
        if (optionalUsuario.isEmpty()) {
            return new LoginResponse(false, null, "Usuário não encontrado.", null);
        }

        Usuario usuario = optionalUsuario.get();
        // Verifica se a senha informada bate com a senha armazenada (criptografada)
        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            return new LoginResponse(false, null, "Senha incorreta.", null);
        }

        // Se tudo estiver correto, retorna sucesso com informações do usuário
        return new LoginResponse(true, usuario.getTipoUsuario().name(), "Login bem-sucedido!", usuario.getId());
    }
}
