package com.copalpite.controller;

import com.copalpite.dto.*;
import com.copalpite.service.bolao.BolaoService;
import com.copalpite.service.participante.BolaoParticipanteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bolaos")
@RequiredArgsConstructor
public class BolaoController {

    private final BolaoService bolaoService;
    private final BolaoParticipanteService participanteService;

    @PostMapping
    public ResponseEntity<BolaoRespostaDTO> criar(@Valid @RequestBody BolaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bolaoService.criar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BolaoRespostaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(bolaoService.buscarPorId(id));
    }

    @GetMapping("/convite/{codigo}")
    public ResponseEntity<BolaoRespostaDTO> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(bolaoService.buscarPorCodigoConvite(codigo));
    }

    @GetMapping
    public ResponseEntity<List<BolaoRespostaDTO>> listarTodos() {
        return ResponseEntity.ok(bolaoService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        bolaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/renovar-convite")
    public ResponseEntity<String> renovarConvite(@PathVariable Long id) {
        return ResponseEntity.ok(bolaoService.gerarNovoCodigoConvite(id));
    }


    @PostMapping("/entrar")
    public ResponseEntity<ParticipanteRespostaDTO> entrar(@Valid @RequestBody EntradaBolaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(participanteService.entrarNoBolao(dto));
    }

    @DeleteMapping("/{bolaoId}/participantes/{usuarioId}")
    public ResponseEntity<Void> sair(@PathVariable Long bolaoId, @PathVariable Long usuarioId) {
        participanteService.sairDoBolao(bolaoId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bolaoId}/participantes")
    public ResponseEntity<List<ParticipanteRespostaDTO>> listarParticipantes(@PathVariable Long bolaoId) {
        return ResponseEntity.ok(participanteService.listarParticipantes(bolaoId));
    }

    @GetMapping("/{bolaoId}/ranking")
    public ResponseEntity<List<ParticipanteRespostaDTO>> ranking(@PathVariable Long bolaoId) {
        return ResponseEntity.ok(participanteService.rankingDoBolao(bolaoId));
    }
}
