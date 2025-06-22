package com.example.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException;

/**
 * Componente que testa a conexão com o banco de dados na inicialização da aplicação.
 * Implementa {@link CommandLineRunner} para executar um teste de conexão
 * logo após o contexto Spring ser carregado.
 */
@Component
public class DatabaseConnectionTester implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Construtor para injeção de dependência do JdbcTemplate.
     *
     * @param jdbcTemplate O template JDBC fornecido pelo Spring para interagir com o banco de dados.
     */
    public DatabaseConnectionTester(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Executa o teste de conexão com o banco de dados.
     * Tenta consultar o número total de usuários na tabela 'usuarios'
     * para verificar se a conexão está ativa e funcional.
     *
     * @param args Argumentos de linha de comando (não utilizados neste contexto).
     */
    @Override
    public void run(String... args) {
        System.out.println("Testing database connection..."); // More generic message
        try {
            // Tenta executar uma consulta simples para verificar a conectividade
            Integer totalUsers = jdbcTemplate.queryForObject("SELECT count(*) FROM usuarios", Integer.class);
            System.out.println("Database connection successful. Total users in 'usuarios' table: " + totalUsers);
        } catch (DataAccessException e) {
            // Captura exceções relacionadas a problemas de acesso a dados
            System.err.println("Error accessing the database: " + e.getMessage());
            System.err.println("Please check your database connection configuration (e.g., application.properties/yml).");
        }
    }
}