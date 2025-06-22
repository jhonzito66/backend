package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "maquinarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Maquinario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING) // Armazena o enum como String
    @Column(nullable = false, name = "status")
    private MaquinarioStatus status;

    @Column(nullable = false)
    private Boolean lavado;

    @Column(nullable = false)
    private Boolean abastecido;

    // Relacionamento OneToMany com TarefaMaquinario
    // FetchType.LAZY para evitar carregamento ansioso desnecessário
    @OneToMany(mappedBy = "maquinario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<TarefaMaquinario> tarefasMaquinarios;
}
