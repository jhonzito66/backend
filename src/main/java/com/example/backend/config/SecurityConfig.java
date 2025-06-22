package com.example.backend.config;

import com.example.backend.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração de segurança principal para a aplicação Spring.
 * Habilita a segurança a nível de método e define a cadeia de filtros de segurança,
 * o provedor de autenticação e o codificador de senhas.
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Construtor para injeção de dependência do UserDetailsServiceImpl.
     *
     * @param userDetailsService O serviço customizado para carregar dados do usuário.
     */
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Define a cadeia de filtros de segurança que governa as regras de autorização HTTP.
     *
     * @param http O objeto HttpSecurity para configurar a segurança web.
     * @return A instância de SecurityFilterChain construída.
     * @throws Exception se ocorrer um erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF, comum para APIs RESTful.
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público às rotas de registro e login.
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        // Exige autenticação para todas as outras requisições.
                        .anyRequest().authenticated()
                )
                // Habilita a autenticação HTTP Basic como método de autenticação.
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Cria um bean para o PasswordEncoder que utiliza o algoritmo BCrypt.
     *
     * @return Uma instância de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura e expõe o provedor de autenticação principal da aplicação.
     * Utiliza o DaoAuthenticationProvider para integrar com o UserDetailsService
     * e o PasswordEncoder customizados.
     *
     * @param passwordEncoder O codificador de senhas a ser usado para verificar as senhas.
     * @return Uma instância configurada do AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}