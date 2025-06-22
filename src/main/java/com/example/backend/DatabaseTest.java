package com.example.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class DatabaseTest implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) FROM information_schema.tables", Integer.class);
        System.out.println("Número de tabelas no banco: " + count);
    }
}

