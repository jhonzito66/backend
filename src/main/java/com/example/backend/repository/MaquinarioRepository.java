package com.example.backend.repository;

import com.example.backend.model.Maquinario;
import com.example.backend.model.MaquinarioStatus; // Import para o enum MaquinarioStatus
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositório para gerenciar a entidade Maquinario no banco de dados
public interface MaquinarioRepository extends JpaRepository<Maquinario, Long> {

    // Busca uma lista de Maquinarios filtrando pelo status
    List<Maquinario> findByStatus(MaquinarioStatus status);
}
