package com.example.backend.controller;

import com.example.backend.dto.InsumoRequest;
import com.example.backend.dto.InsumoResponse;
import com.example.backend.dto.MaquinarioRequest;
import com.example.backend.dto.MaquinarioResponse;
import com.example.backend.dto.TarefaFinalizacaoRequest;
import com.example.backend.dto.TarefaRequest;
import com.example.backend.dto.TarefaResponse;
import com.example.backend.dto.UsuarioResponseDTO;
import com.example.backend.model.StatusTarefa;
import com.example.backend.model.Usuario;
import com.example.backend.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

// Controlador REST para a API de tarefas, insumos e maquinários.
@RestController
@RequestMapping("/api/tarefas") // Mantemos o mesmo prefixo para simplificar.
public class TarefaController {

    private final TarefaService tarefaService;

    @Autowired
    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    // --- Endpoints de Gerenciamento de Tarefas ---

    // Endpoint para criar uma nova tarefa.
    // Apenas ADMIN e MODERADOR podem criar tarefas.
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<TarefaResponse> criarTarefa(@RequestBody TarefaRequest request,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        // O ID do atribuidor deve vir do usuário autenticado.
        Usuario authenticatedUser = (Usuario) userDetails; // Cast para o nosso tipo Usuario customizado
        request.setAtribuidorId(authenticatedUser.getId());

        TarefaResponse novaTarefa = tarefaService.criarTarefa(request);
        return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
    }

    // Endpoint para listar todas as tarefas.
    // Apenas ADMIN e MODERADOR podem ver todas as tarefas.
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<TarefaResponse>> listarTodasTarefas() {
        List<TarefaResponse> tarefas = tarefaService.listarTodasTarefas();
        return ResponseEntity.ok(tarefas);
    }

    // Endpoint para listar as tarefas atribuídas a um usuário específico.
    // Usuários COMUNS só podem ver suas próprias tarefas.
    // ADMIN e MODERADOR podem ver tarefas de qualquer usuário.
    @GetMapping("/atribuidas/{atributarioId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR') or (#atributarioId == principal.id)")
    public ResponseEntity<List<TarefaResponse>> listarTarefasPorAtributario(@PathVariable Long atributarioId,
                                                                            @AuthenticationPrincipal UserDetails userDetails) {
        List<TarefaResponse> tarefas = tarefaService.listarTarefasPorAtributario(atributarioId);
        return ResponseEntity.ok(tarefas);
    }

    // Endpoint para atualizar o status de uma tarefa.
    // ADMIN e MODERADOR podem mudar o status de qualquer tarefa.
    // COMUM pode mudar o status de suas próprias tarefas para CONCLUIDA ou EM_ANDAMENTO.
    @PutMapping("/{id}/status/{newStatus}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR') or (hasAuthority('COMUM') and @tarefaService.isTarefaOwner(principal.id, #id))")
    public ResponseEntity<TarefaResponse> atualizarStatusTarefa(@PathVariable Long id, @PathVariable String newStatus,
                                                                @AuthenticationPrincipal UserDetails userDetails) {
        Usuario authenticatedUser = (Usuario) userDetails;
        StatusTarefa statusEnum;
        try {
            statusEnum = StatusTarefa.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + newStatus);
        }

        if (authenticatedUser.getTipoUsuario().equals(Usuario.TipoUsuario.COMUM)) {
            if (statusEnum.equals(StatusTarefa.PENDENTE)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuários comuns não podem reverter o status para PENDENTE.");
            }
        }

        TarefaResponse updatedTarefa = tarefaService.atualizarStatusTarefa(id, statusEnum);
        return ResponseEntity.ok(updatedTarefa);
    }

    // Endpoint para finalizar uma tarefa (agora recebe detalhes de insumo/maquinário)
    // Permite que usuários COMUNS ou MODERADORES finalizem suas próprias tarefas.
    @PutMapping("/{id}/finalizar")
    @PreAuthorize("(hasAuthority('COMUM') or hasAuthority('MODERADOR')) and @tarefaService.isTarefaOwner(principal.id, #id)")
    public ResponseEntity<TarefaResponse> finalizarTarefa(@PathVariable Long id,
                                                          @RequestBody TarefaFinalizacaoRequest request,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        TarefaResponse finalizedTarefa = tarefaService.finalizarTarefa(
                id,
                request.getObservacao(),
                request.getInsumosEntregues(),
                request.getMaquinariosDevolvidos()
        );
        return ResponseEntity.ok(finalizedTarefa);
    }

    // Endpoint para listar todas as tarefas concluídas
    // Apenas ADMIN e MODERADOR podem ver as tarefas concluídas.
    @GetMapping("/concluidas")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<TarefaResponse>> listarTarefasConcluidas() {
        List<TarefaResponse> tarefasConcluidas = tarefaService.listarTarefasConcluidas();
        return ResponseEntity.ok(tarefasConcluidas);
    }

    // Endpoint para listar todos os usuários que podem receber tarefas (COMUM e MODERADOR).
    // Apenas ADMIN e MODERADOR podem listar usuários para atribuição.
    @GetMapping("/usuarios-atribuiveis")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuariosAtribuiveis() {
        List<UsuarioResponseDTO> usuarios = tarefaService.listarUsuariosAtribuiveis();
        return ResponseEntity.ok(usuarios);
    }

    // --- Endpoints de Gerenciamento de Insumos ---

    @PostMapping("/insumos")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<InsumoResponse> criarInsumo(@RequestBody InsumoRequest request) {
        InsumoResponse novoInsumo = tarefaService.criarInsumo(request);
        return new ResponseEntity<>(novoInsumo, HttpStatus.CREATED);
    }

    @GetMapping("/insumos")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<InsumoResponse>> listarTodosInsumos() {
        List<InsumoResponse> insumos = tarefaService.listarTodosInsumos();
        return ResponseEntity.ok(insumos);
    }

    @GetMapping("/insumos/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<InsumoResponse> buscarInsumoPorId(@PathVariable Long id) {
        InsumoResponse insumo = tarefaService.buscarInsumoPorId(id);
        return ResponseEntity.ok(insumo);
    }

    @PutMapping("/insumos/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<InsumoResponse> atualizarInsumo(@PathVariable Long id, @RequestBody InsumoRequest request) {
        InsumoResponse insumoAtualizado = tarefaService.atualizarInsumo(id, request);
        return ResponseEntity.ok(insumoAtualizado);
    }

    @DeleteMapping("/insumos/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<Void> deletarInsumo(@PathVariable Long id) {
        tarefaService.deletarInsumo(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoints de Gerenciamento de Maquinários ---

    @PostMapping("/maquinarios")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<MaquinarioResponse> criarMaquinario(@RequestBody MaquinarioRequest request) {
        MaquinarioResponse novoMaquinario = tarefaService.criarMaquinario(request);
        return new ResponseEntity<>(novoMaquinario, HttpStatus.CREATED);
    }

    @GetMapping("/maquinarios")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<MaquinarioResponse>> listarTodosMaquinarios() {
        List<MaquinarioResponse> maquinarios = tarefaService.listarTodosMaquinarios();
        return ResponseEntity.ok(maquinarios);
    }

    @GetMapping("/maquinarios/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<MaquinarioResponse> buscarMaquinarioPorId(@PathVariable Long id) {
        MaquinarioResponse maquinario = tarefaService.buscarMaquinarioPorId(id);
        return ResponseEntity.ok(maquinario);
    }

    @PutMapping("/maquinarios/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<MaquinarioResponse> atualizarMaquinario(@PathVariable Long id, @RequestBody MaquinarioRequest request) {
        MaquinarioResponse maquinarioAtualizado = tarefaService.atualizarMaquinario(id, request);
        return ResponseEntity.ok(maquinarioAtualizado);
    }

    @DeleteMapping("/maquinarios/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<Void> deletarMaquinario(@PathVariable Long id) {
        tarefaService.deletarMaquinario(id);
        return ResponseEntity.noContent().build();
    }
}