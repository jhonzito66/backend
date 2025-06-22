package com.example.backend.repository;

import com.example.backend.model.TarefaMaquinario;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório para a entidade TarefaMaquinario.
public interface TarefaMaquinarioRepository extends JpaRepository<TarefaMaquinario, Long> {
    java.util.List<TarefaMaquinario> findByTarefaId(Long tarefaId);
}
