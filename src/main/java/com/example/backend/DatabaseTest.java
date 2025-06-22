package com.example.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DataAccessException; // Import DataAccessException for better error handling

/**
 * Componente que testa a conectividade e o estado inicial do banco de dados na inicialização da aplicação.
 * Implementa {@link CommandLineRunner} para executar um teste de conexão
 * logo após o contexto Spring ser carregado, verificando se a aplicação
 * consegue se comunicar com o banco de dados e obter informações básicas.
 */
@Component
public class DatabaseTest implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Construtor para injeção de dependência do JdbcTemplate.
     * O Spring Boot configura e fornece automaticamente uma instância de JdbcTemplate
     * conectada ao banco de dados configurado em `application.properties` ou `application.yml`.
     *
     * @param jdbcTemplate O template JDBC fornecido pelo Spring para interagir com o banco de dados.
     */
    public DatabaseTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Executa o teste de conexão com o banco de dados.
     * Este método é invocado automaticamente pelo Spring Boot após a inicialização da aplicação.
     * Ele tenta executar uma consulta simples para verificar se a conexão está ativa e funcional,
     * e imprime o resultado no console.
     *
     * @param args Argumentos de linha de comando (não utilizados neste contexto).
     * @throws Exception Em caso de erro irrecuperável durante a execução do teste.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Starting database connection test...");
        try {
            // Tenta executar uma consulta simples para verificar a conectividade
            // e o número de tabelas no schema padrão.
            // Para PostgreSQL/H2/MySQL, 'information_schema.tables' é comum.
            Integer tableCount = jdbcTemplate.queryForObject("SELECT count(*) FROM information_schema.tables", Integer.class);
            System.out.println("Database connection successful. Number of tables found: " + tableCount);
        } catch (DataAccessException e) {
            // Captura exceções relacionadas a problemas de acesso a dados (e.g., credenciais erradas, banco offline).
            System.err.println("Error accessing the database. Please check your connection settings in application.properties/yml.");
            System.err.println("Details: " + e.getMessage());
            // It's often good practice to re-throw a RuntimeException here if a failed DB connection
            // should prevent the application from starting in production environments.
            // throw new RuntimeException("Failed to connect to the database on startup.", e);
        } catch (Exception e) {
            // Captura qualquer outra exceção inesperada.
            System.err.println("An unexpected error occurred during the database test: " + e.getMessage());
            throw e; // Re-throw to indicate a critical startup failure
        }
    }
}