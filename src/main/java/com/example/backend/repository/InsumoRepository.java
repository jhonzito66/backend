package com.example.backend.repository;

import com.example.backend.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório para a entidade Insumo.
public interface InsumoRepository extends JpaRepository<Insumo, Long> {
}
