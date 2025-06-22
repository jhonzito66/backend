package com.example.backend.repository;

import com.example.backend.model.TarefaInsumo;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório para gerenciar operações CRUD da entidade TarefaInsumo
public interface TarefaInsumoRepository extends JpaRepository<TarefaInsumo, Long> {

    // Método para buscar lista de TarefaInsumo pelo ID da tarefa
    java.util.List<TarefaInsumo> findByTarefaId(Long tarefaId);
}
