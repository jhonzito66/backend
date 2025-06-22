package com.example.backend.repository;

import com.example.backend.model.StatusTarefa; // Novo import
import com.example.backend.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositório para a entidade Tarefa.
// Fornece métodos de CRUD e consulta para tarefas.
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    // Busca todas as tarefas atribuídas a um usuário específico.
    List<Tarefa> findByAtributarioId(Long atributarioId);

    // Novo método para buscar tarefas por status
    List<Tarefa> findByStatus(StatusTarefa status);
}
