package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
    private Boolean lavado;         // Indica se o maquinário está lavado

    @Column(nullable = false)
    private Boolean abastecido;     // Indica se o maquinário está abastecido

    // Relação com tarefas que utilizam este maquinário
    @OneToMany(mappedBy = "maquinario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<TarefaMaquinario> tarefasMaquinarios; // Lista de tarefas vinculadas
}
