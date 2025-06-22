package com.example.backend.repository;

import com.example.backend.model.Maquinario;
import com.example.backend.model.MaquinarioStatus; // Import para MaquinarioStatus
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repositório para a entidade Maquinario.
public interface MaquinarioRepository extends JpaRepository<Maquinario, Long> {
    List<Maquinario> findByStatus(MaquinarioStatus status); // Para filtrar por status
}
