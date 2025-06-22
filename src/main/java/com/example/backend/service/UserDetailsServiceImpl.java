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

/**
 * Implementação personalizada da interface {@link UserDetailsService} do Spring Security.
 * Responsável por carregar os detalhes do usuário do banco de dados para o processo de autenticação.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Construtor para injeção de dependências do repositório de usuários.
     *
     * @param usuarioRepository O repositório para acessar os dados dos usuários.
     */
    @Autowired
    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Carrega os detalhes de um usuário pelo seu email (que é usado como username para autenticação).
     * Este método é invocado pelo Spring Security durante o processo de autenticação.
     *
     * @param email O email do usuário (username) a ser carregado.
     * @return Um objeto {@link UserDetails} representando o usuário autenticado.
     * @throws UsernameNotFoundException Se o usuário com o email fornecido não for encontrado no banco de dados.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário pelo email no repositório.
        // Se não for encontrado, lança uma exceção UsernameNotFoundException.
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        // Retorna o próprio objeto Usuario, que já implementa a interface UserDetails.
        // O tipo de usuário (e, consequentemente, suas autoridades/permissões)
        // é tratado dentro do objeto Usuario em si.
        return usuario;
    }
}