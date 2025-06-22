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
@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    @Autowired
    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    // Cria uma nova tarefa. Apenas ADMIN e MODERADOR podem executar esta ação.
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<TarefaResponse> criarTarefa(@RequestBody TarefaRequest request,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        Usuario authenticatedUser = (Usuario) userDetails;
        request.setAtribuidorId(authenticatedUser.getId());
        TarefaResponse novaTarefa = tarefaService.criarTarefa(request);
        return new ResponseEntity<>(novaTarefa, HttpStatus.CREATED);
    }

    // Lista todas as tarefas do sistema. Acesso restrito a ADMIN e MODERADOR.
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<TarefaResponse>> listarTodasTarefas() {
        return ResponseEntity.ok(tarefaService.listarTodasTarefas());
    }

    // Lista as tarefas atribuídas a um determinado usuário.
    // ADMIN e MODERADOR podem ver tarefas de qualquer usuário;
    // Usuários comuns podem ver apenas suas próprias.
    @GetMapping("/atribuidas/{atributarioId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR') or (#atributarioId == principal.id)")
    public ResponseEntity<List<TarefaResponse>> listarTarefasPorAtributario(@PathVariable Long atributarioId,
                                                                            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(tarefaService.listarTarefasPorAtributario(atributarioId));
    }

    // Atualiza o status de uma tarefa.
    // ADMIN e MODERADOR podem alterar qualquer tarefa;
    // Usuários comuns podem apenas alterar o status de suas tarefas para EM_ANDAMENTO ou CONCLUIDA.
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

        return ResponseEntity.ok(tarefaService.atualizarStatusTarefa(id, statusEnum));
    }

    // Finaliza uma tarefa com observação, insumos entregues e maquinários devolvidos.
    // Permitido para usuários COMUM e MODERADOR que sejam donos da tarefa.
    @PutMapping("/{id}/finalizar")
    @PreAuthorize("(hasAuthority('COMUM') or hasAuthority('MODERADOR')) and @tarefaService.isTarefaOwner(principal.id, #id)")
    public ResponseEntity<TarefaResponse> finalizarTarefa(@PathVariable Long id,
                                                          @RequestBody TarefaFinalizacaoRequest request,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                tarefaService.finalizarTarefa(id, request.getObservacao(), request.getInsumosEntregues(), request.getMaquinariosDevolvidos())
        );
    }

    // Lista todas as tarefas que estão com status CONCLUIDA.
    // Apenas ADMIN e MODERADOR têm acesso.
    @GetMapping("/concluidas")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<TarefaResponse>> listarTarefasConcluidas() {
        return ResponseEntity.ok(tarefaService.listarTarefasConcluidas());
    }

    // Lista todos os usuários que podem receber tarefas (tipos COMUM e MODERADOR).
    // Visível apenas para ADMIN e MODERADOR.
    @GetMapping("/usuarios-atribuiveis")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuariosAtribuiveis() {
        return ResponseEntity.ok(tarefaService.listarUsuariosAtribuiveis());
    }

    // Cria um novo insumo. Acesso restrito a ADMIN e MODERADOR.
    @PostMapping("/insumos")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<InsumoResponse> criarInsumo(@RequestBody InsumoRequest request) {
        return new ResponseEntity<>(tarefaService.criarInsumo(request), HttpStatus.CREATED);
    }

    // Lista todos os insumos cadastrados. Acesso restrito a ADMIN e MODERADOR.
    @GetMapping("/insumos")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<InsumoResponse>> listarTodosInsumos() {
        return ResponseEntity.ok(tarefaService.listarTodosInsumos());
    }

    // Busca um insumo específico pelo seu ID. Apenas ADMIN e MODERADOR têm acesso.
    @GetMapping("/insumos/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<InsumoResponse> buscarInsumoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tarefaService.buscarInsumoPorId(id));
    }

    // Atualiza os dados de um insumo existente. Apenas ADMIN e MODERADOR têm acesso.
    @PutMapping("/insumos/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<InsumoResponse> atualizarInsumo(@PathVariable Long id, @RequestBody InsumoRequest request) {
        return ResponseEntity.ok(tarefaService.atualizarInsumo(id, request));
    }

    // Remove um insumo pelo ID. Apenas ADMIN e MODERADOR têm acesso.
    @DeleteMapping("/insumos/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<Void> deletarInsumo(@PathVariable Long id) {
        tarefaService.deletarInsumo(id);
        return ResponseEntity.noContent().build();
    }

    // Cria um novo maquinário. Apenas ADMIN e MODERADOR têm acesso.
    @PostMapping("/maquinarios")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<MaquinarioResponse> criarMaquinario(@RequestBody MaquinarioRequest request) {
        return new ResponseEntity<>(tarefaService.criarMaquinario(request), HttpStatus.CREATED);
    }

    // Lista todos os maquinários cadastrados. Apenas ADMIN e MODERADOR têm acesso.
    @GetMapping("/maquinarios")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<List<MaquinarioResponse>> listarTodosMaquinarios() {
        return ResponseEntity.ok(tarefaService.listarTodosMaquinarios());
    }

    // Busca um maquinário pelo ID. Apenas ADMIN e MODERADOR têm acesso.
    @GetMapping("/maquinarios/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<MaquinarioResponse> buscarMaquinarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tarefaService.buscarMaquinarioPorId(id));
    }

    // Atualiza os dados de um maquinário existente. Apenas ADMIN e MODERADOR têm acesso.
    @PutMapping("/maquinarios/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<MaquinarioResponse> atualizarMaquinario(@PathVariable Long id, @RequestBody MaquinarioRequest request) {
        return ResponseEntity.ok(tarefaService.atualizarMaquinario(id, request));
    }

    // Remove um maquinário do sistema. Apenas ADMIN e MODERADOR têm acesso.
    @DeleteMapping("/maquinarios/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
    public ResponseEntity<Void> deletarMaquinario(@PathVariable Long id) {
        tarefaService.deletarMaquinario(id);
        return ResponseEntity.noContent().build();
    }
}
