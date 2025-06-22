package com.example.backend.service;

import com.example.backend.model.Usuario;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

// Implementação do UserDetailsService do Spring Security.
// Carrega os detalhes do usuário do banco de dados para a autenticação.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        // --- Log de Depuração ---
        System.out.println("--- UserDetailsServiceImpl ---");
        System.out.println("Usuário carregado: " + usuario.getEmail());
        System.out.println("Tipo de Usuário: " + usuario.getTipoUsuario().name());
        System.out.println("Authorities concedidas: " + Collections.singletonList(new SimpleGrantedAuthority(usuario.getTipoUsuario().name())));
        System.out.println("------------------------------");
        // --------------------------

        // Converte o tipo de usuário (enum) em uma Authority para o Spring Security.
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuario.getTipoUsuario().name());

        // Retorna o próprio objeto Usuario como UserDetails.
        return usuario;
    }
}
