package com.example.backend.repository;

import com.example.backend.model.StatusTarefa; // Importa o enum de status da tarefa
import com.example.backend.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositório para gerenciar a entidade Tarefa no banco de dados
// Fornece métodos CRUD e consultas específicas para tarefas
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // Busca todas as tarefas atribuídas a um usuário pelo ID do atributário
    List<Tarefa> findByAtributarioId(Long atributarioId);

    // Busca todas as tarefas que possuem um determinado status
    List<Tarefa> findByStatus(StatusTarefa status);
}
