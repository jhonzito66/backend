package com.example.backend.repository;

import com.example.backend.model.TarefaInsumo;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório para a entidade TarefaInsumo.
public interface TarefaInsumoRepository extends JpaRepository<TarefaInsumo, Long> {
    java.util.List<TarefaInsumo> findByTarefaId(Long tarefaId);
}
