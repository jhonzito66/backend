package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entidade que representa um Maquinário no sistema.
 * Mapeada para a tabela 'maquinarios' no banco de dados.
 */
@Entity
@Table(name = "maquinarios")
@Data // Gera getters, setters, equals, hashCode e toString
@NoArgsConstructor // Construtor padrão
@AllArgsConstructor // Construtor com todos os campos
public class Maquinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // Identificador único do maquinário

    @Column(nullable = false)
    private String nome;            // Nome do maquinário

    @Column(columnDefinition = "TEXT")
    private String descricao;       // Descrição detalhada do maquinário

    @Enumerated(EnumType.STRING) // Salva enum como string no banco
    @Column(nullable = false, name = "status")
    private MaquinarioStatus status; // Status atual (DISPONIVEL, EM_USO, MANUTENCAO)

    @Column(nullable = false)
    private boolean lavado;         // **ALTERADO**: Agora é 'boolean' primitivo (não aceita null)

    @Column(nullable = false)
    private boolean abastecido;     // **ALTERADO**: Agora é 'boolean' primitivo (não aceita null)

    // Relação com tarefas que utilizam este maquinário
    @OneToMany(mappedBy = "maquinario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<TarefaMaquinario> tarefasMaquinarios;
}