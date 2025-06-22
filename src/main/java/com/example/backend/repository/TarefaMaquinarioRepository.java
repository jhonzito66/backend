package com.example.backend.repository;

import com.example.backend.model.TarefaMaquinario;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositório para gerenciar a entidade TarefaMaquinario no banco de dados
public interface TarefaMaquinarioRepository extends JpaRepository<TarefaMaquinario, Long> {

    // Busca todos os TarefaMaquinario associados a uma tarefa pelo ID da tarefa
    java.util.List<TarefaMaquinario> findByTarefaId(Long tarefaId);
}
