package com.example.backend.service;

import com.example.backend.dto.InsumoRequest;
import com.example.backend.dto.InsumoResponse;
import com.example.backend.dto.MaquinarioRequest;
import com.example.backend.dto.MaquinarioResponse;
import com.example.backend.dto.TarefaFinalizacaoRequest;
import com.example.backend.dto.TarefaRequest;
import com.example.backend.dto.TarefaResponse;
import com.example.backend.dto.TarefaInsumoRequest;
import com.example.backend.dto.TarefaInsumoResponse;
import com.example.backend.dto.TarefaMaquinarioRequest;
import com.example.backend.dto.TarefaMaquinarioResponse;
import com.example.backend.dto.UsuarioResponseDTO;
import com.example.backend.model.Insumo;
import com.example.backend.model.Maquinario;
import com.example.backend.model.MaquinarioStatus;
import com.example.backend.model.StatusTarefa;
import com.example.backend.model.Tarefa;
import com.example.backend.model.TarefaInsumo;
import com.example.backend.model.TarefaMaquinario;
import com.example.backend.model.Usuario;
import com.example.backend.repository.InsumoRepository;
import com.example.backend.repository.MaquinarioRepository;
import com.example.backend.repository.TarefaInsumoRepository;
import com.example.backend.repository.TarefaMaquinarioRepository;
import com.example.backend.repository.TarefaRepository;
import com.example.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;
    private final InsumoRepository insumoRepository;
    private final MaquinarioRepository maquinarioRepository;
    private final TarefaInsumoRepository tarefaInsumoRepository;
    private final TarefaMaquinarioRepository tarefaMaquinarioRepository;

    @Autowired
    public TarefaService(TarefaRepository tarefaRepository,
                         UsuarioRepository usuarioRepository,
                         InsumoRepository insumoRepository,
                         MaquinarioRepository maquinarioRepository,
                         TarefaInsumoRepository tarefaInsumoRepository,
                         TarefaMaquinarioRepository tarefaMaquinarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.usuarioRepository = usuarioRepository;
        this.insumoRepository = insumoRepository;
        this.maquinarioRepository = maquinarioRepository;
        this.tarefaInsumoRepository = tarefaInsumoRepository;
        this.tarefaMaquinarioRepository = tarefaMaquinarioRepository;
    }

    @Transactional
    public TarefaResponse criarTarefa(TarefaRequest request) {
        Usuario atribuidor = usuarioRepository.findById(request.getAtribuidorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atribuidor não encontrado."));

        Usuario atributario = usuarioRepository.findById(request.getAtributarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atributário não encontrado."));

        // MODIFICAÇÃO AQUI: Permitir atribuir a COMUM ou MODERADOR
        if (!atributario.getTipoUsuario().equals(Usuario.TipoUsuario.COMUM) &&
                !atributario.getTipoUsuario().equals(Usuario.TipoUsuario.MODERADOR)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As tarefas só podem ser atribuídas a usuários do tipo COMUM ou MODERADOR.");
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(request.getTitulo());
        tarefa.setDescricao(request.getDescricao());
        tarefa.setDataLimite(request.getDataLimite());
        tarefa.setAtribuidor(atribuidor);
        tarefa.setAtributario(atributario);
        // Status e dataCriacao são definidos pelo @PrePersist

        Tarefa savedTarefa = tarefaRepository.save(tarefa);

        // Atribuir insumos à tarefa
        if (request.getInsumosAtribuidos() != null && !request.getInsumosAtribuidos().isEmpty()) {
            for (TarefaInsumoRequest insumoReq : request.getInsumosAtribuidos()) {
                Insumo insumo = insumoRepository.findById(insumoReq.getInsumoId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Insumo não encontrado: " + insumoReq.getInsumoId()));

                if (insumo.getQuantidade().compareTo(insumoReq.getQuantidade()) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantidade de insumo " + insumo.getNome() + " insuficiente.");
                }
                TarefaInsumo tarefaInsumo = new TarefaInsumo();
                tarefaInsumo.setTarefa(savedTarefa);
                tarefaInsumo.setInsumo(insumo);
                tarefaInsumo.setQuantidade(insumoReq.getQuantidade());
                tarefaInsumoRepository.save(tarefaInsumo);

                insumo.setQuantidade(insumo.getQuantidade().subtract(insumoReq.getQuantidade()));
                insumoRepository.save(insumo);
            }
        }

        // Atribuir maquinários à tarefa
        if (request.getMaquinariosAtribuidos() != null && !request.getMaquinariosAtribuidos().isEmpty()) {
            for (TarefaMaquinarioRequest maquinarioReq : request.getMaquinariosAtribuidos()) {
                Maquinario maquinario = maquinarioRepository.findById(maquinarioReq.getMaquinarioId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Maquinário não encontrado: " + maquinarioReq.getMaquinarioId()));

                if (maquinario.getStatus() != MaquinarioStatus.DISPONIVEL) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maquinário " + maquinario.getNome() + " não está disponível para uso.");
                }

                TarefaMaquinario tarefaMaquinario = new TarefaMaquinario();
                tarefaMaquinario.setTarefa(savedTarefa);
                tarefaMaquinario.setMaquinario(maquinario);
                tarefaMaquinario.setQuantidade(maquinarioReq.getQuantidade());
                tarefaMaquinarioRepository.save(tarefaMaquinario);

                maquinario.setStatus(MaquinarioStatus.EM_USO);
                maquinarioRepository.save(maquinario);
            }
        }

        Optional<Tarefa> reloadedTarefa = tarefaRepository.findById(savedTarefa.getId());
        if (reloadedTarefa.isPresent()) {
            return mapToTarefaResponse(reloadedTarefa.get());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao recarregar tarefa após criação.");
        }
    }

    public List<TarefaResponse> listarTodasTarefas() {
        return tarefaRepository.findAll().stream()
                .map(this::mapToTarefaResponse)
                .collect(Collectors.toList());
    }

    public List<TarefaResponse> listarTarefasPorAtributario(Long atributarioId) {
        Usuario atributario = usuarioRepository.findById(atributarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário atributário não encontrado."));

        return tarefaRepository.findByAtributarioId(atributarioId).stream()
                .map(this::mapToTarefaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TarefaResponse atualizarStatusTarefa(Long tarefaId, StatusTarefa newStatus) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada."));

        tarefa.setStatus(newStatus);
        if (newStatus == StatusTarefa.CONCLUIDA) {
            tarefa.setDataConclusao(LocalDateTime.now());
        }
        Tarefa updatedTarefa = tarefaRepository.save(tarefa);
        return mapToTarefaResponse(updatedTarefa);
    }

    @Transactional
    public TarefaResponse finalizarTarefa(Long tarefaId, String observacaoFinalizacao,
                                          List<TarefaFinalizacaoRequest.InsumoEntregueRequest> insumosEntregues,
                                          List<TarefaFinalizacaoRequest.MaquinarioDevolvidoRequest> maquinariosDevolvidos) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada."));

        if (tarefa.getStatus() == StatusTarefa.CONCLUIDA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tarefa já está concluída.");
        }

        tarefa.setStatus(StatusTarefa.CONCLUIDA);
        tarefa.setObservacaoFinalizacao(observacaoFinalizacao);
        tarefa.setDataConclusao(LocalDateTime.now());

        if (insumosEntregues != null) {
            for (TarefaFinalizacaoRequest.InsumoEntregueRequest req : insumosEntregues) {
                TarefaInsumo tarefaInsumo = tarefaInsumoRepository.findById(req.getTarefaInsumoId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de insumo da tarefa não encontrado: " + req.getTarefaInsumoId()));

                if (!tarefaInsumo.getTarefa().getId().equals(tarefaId)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insumo não pertence a esta tarefa.");
                }

                tarefaInsumo.setEntregue(req.getEntregue());
                if (req.getEntregue()) {
                    tarefaInsumo.setDataEntrega(LocalDateTime.now());
                }
                tarefaInsumoRepository.save(tarefaInsumo);
            }
        }

        if (maquinariosDevolvidos != null) {
            for (TarefaFinalizacaoRequest.MaquinarioDevolvidoRequest req : maquinariosDevolvidos) {
                TarefaMaquinario tarefaMaquinario = tarefaMaquinarioRepository.findById(req.getTarefaMaquinarioId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item de maquinário da tarefa não encontrado: " + req.getTarefaMaquinarioId()));

                if (!tarefaMaquinario.getTarefa().getId().equals(tarefaId)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maquinário não pertence a esta tarefa.");
                }

                tarefaMaquinario.setFoiLavado(req.getFoiLavado());
                tarefaMaquinario.setFoiAbastecido(req.getFoiAbastecido());
                tarefaMaquinario.setDataDevolucao(LocalDateTime.now());
                tarefaMaquinarioRepository.save(tarefaMaquinario);

                Maquinario maquinario = tarefaMaquinario.getMaquinario();
                maquinario.setStatus(MaquinarioStatus.DISPONIVEL);
                maquinario.setLavado(req.getFoiLavado());
                maquinario.setAbastecido(req.getFoiAbastecido());
                maquinarioRepository.save(maquinario);
            }
        }

        Tarefa finalizedTarefa = tarefaRepository.save(tarefa);
        return mapToTarefaResponse(finalizedTarefa);
    }

    public boolean isTarefaOwner(Long userId, Long taskId) {
        return tarefaRepository.findById(taskId)
                .map(tarefa -> tarefa.getAtributario().getId().equals(userId))
                .orElse(false);
    }

    public List<TarefaResponse> listarTarefasConcluidas() {
        return tarefaRepository.findByStatus(StatusTarefa.CONCLUIDA).stream()
                .map(this::mapToTarefaResponse)
                .collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> listarUsuariosAtribuiveis() {
        return usuarioRepository.findByTipoUsuarioIn(List.of(Usuario.TipoUsuario.COMUM, Usuario.TipoUsuario.MODERADOR)).stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getNome(), u.getEmail(), u.getTipoUsuario()))
                .collect(Collectors.toList());
    }

    // --- Métodos de CRUD para Insumos (Adicionado para a nova funcionalidade) ---
    @Transactional
    public InsumoResponse criarInsumo(InsumoRequest request) {
        Insumo insumo = new Insumo();
        insumo.setNome(request.getNome());
        insumo.setDescricao(request.getDescricao());
        insumo.setUnidadeMedida(request.getUnidadeMedida());
        insumo.setQuantidade(request.getQuantidade());
        insumo.setDataValidade(request.getDataValidade());
        Insumo savedInsumo = insumoRepository.save(insumo);
        return mapToInsumoResponse(savedInsumo);
    }

    public List<InsumoResponse> listarTodosInsumos() {
        return insumoRepository.findAll().stream()
                .map(this::mapToInsumoResponse)
                .collect(Collectors.toList());
    }

    public InsumoResponse buscarInsumoPorId(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Insumo não encontrado."));
        return mapToInsumoResponse(insumo);
    }

    @Transactional
    public InsumoResponse atualizarInsumo(Long id, InsumoRequest request) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Insumo não encontrado."));
        insumo.setNome(request.getNome());
        insumo.setDescricao(request.getDescricao());
        insumo.setUnidadeMedida(request.getUnidadeMedida());
        insumo.setQuantidade(request.getQuantidade());
        insumo.setDataValidade(request.getDataValidade());
        Insumo updatedInsumo = insumoRepository.save(insumo);
        return mapToInsumoResponse(updatedInsumo);
    }

    public void deletarInsumo(Long id) {
        if (!insumoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Insumo não encontrado para deletar.");
        }
        insumoRepository.deleteById(id);
    }

    // --- Métodos de CRUD para Maquinarios (Adicionado para a nova funcionalidade) ---
    @Transactional
    public MaquinarioResponse criarMaquinario(MaquinarioRequest request) {
        Maquinario maquinario = new Maquinario();
        maquinario.setNome(request.getNome());
        maquinario.setDescricao(request.getDescricao());
        maquinario.setStatus(request.getStatus());
        maquinario.setLavado(request.getLavado());
        maquinario.setAbastecido(request.getAbastecido());
        Maquinario savedMaquinario = maquinarioRepository.save(maquinario);
        return mapToMaquinarioResponse(savedMaquinario);
    }

    public List<MaquinarioResponse> listarTodosMaquinarios() {
        return maquinarioRepository.findAll().stream()
                .map(this::mapToMaquinarioResponse)
                .collect(Collectors.toList());
    }

    public MaquinarioResponse buscarMaquinarioPorId(Long id) {
        Maquinario maquinario = maquinarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Maquinário não encontrado."));
        return mapToMaquinarioResponse(maquinario);
    }

    @Transactional
    public MaquinarioResponse atualizarMaquinario(Long id, MaquinarioRequest request) {
        Maquinario maquinario = maquinarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Maquinário não encontrado."));
        maquinario.setNome(request.getNome());
        maquinario.setDescricao(request.getDescricao());
        maquinario.setStatus(request.getStatus());
        maquinario.setLavado(request.getLavado());
        maquinario.setAbastecido(request.getAbastecido());
        Maquinario updatedMaquinario = maquinarioRepository.save(maquinario);
        return mapToMaquinarioResponse(updatedMaquinario);
    }

    public void deletarMaquinario(Long id) {
        if (!maquinarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Maquinário não encontrado para deletar.");
        }
        maquinarioRepository.deleteById(id);
    }

    // --- Métodos Auxiliares de Mapeamento (DTOs) ---

    private TarefaResponse mapToTarefaResponse(Tarefa tarefa) {
        List<TarefaInsumoResponse> insumosResp = tarefa.getInsumosAtribuidos() != null ?
                tarefa.getInsumosAtribuidos().stream()
                        .map(ti -> new TarefaInsumoResponse(
                                ti.getId(),
                                ti.getInsumo().getId(),
                                ti.getInsumo().getNome(),
                                ti.getInsumo().getUnidadeMedida(),
                                ti.getQuantidade(),
                                ti.getEntregue(),
                                ti.getDataEntrega()))
                        .collect(Collectors.toList()) : Collections.emptyList();

        List<TarefaMaquinarioResponse> maquinariosResp = tarefa.getMaquinariosAtribuidos() != null ?
                tarefa.getMaquinariosAtribuidos().stream()
                        .map(tm -> new TarefaMaquinarioResponse(
                                tm.getId(),
                                tm.getMaquinario().getId(),
                                tm.getMaquinario().getNome(),
                                tm.getMaquinario().getStatus().name(),
                                tm.getQuantidade(),
                                tm.getDataDevolucao(),
                                tm.getFoiLavado(),
                                tm.getFoiAbastecido()))
                        .collect(Collectors.toList()) : Collections.emptyList();

        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataCriacao(),
                tarefa.getDataLimite(),
                tarefa.getAtribuidor().getId(),
                tarefa.getAtribuidor().getNome(),
                tarefa.getAtributario().getId(),
                tarefa.getAtributario().getNome(),
                tarefa.getObservacaoFinalizacao(),
                tarefa.getDataConclusao(),
                insumosResp,
                maquinariosResp
        );
    }

    private InsumoResponse mapToInsumoResponse(Insumo insumo) {
        return new InsumoResponse(
                insumo.getId(),
                insumo.getNome(),
                insumo.getDescricao(),
                insumo.getUnidadeMedida(),
                insumo.getQuantidade(),
                insumo.getDataValidade()
        );
    }

    private MaquinarioResponse mapToMaquinarioResponse(Maquinario maquinario) {
        return new MaquinarioResponse(
                maquinario.getId(),
                maquinario.getNome(),
                maquinario.getDescricao(),
                maquinario.getStatus(),
                maquinario.getLavado(),
                maquinario.getAbastecido()
        );
    }
}