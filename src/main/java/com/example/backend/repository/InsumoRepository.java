package com.example.backend.repository;

import com.example.backend.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório JPA para gerenciar operações CRUD da entidade Insumo
public interface InsumoRepository extends JpaRepository<Insumo, Long> {
}
