package com.copalpite.controller;

import com.copalpite.dto.PalpiteDTO;
import com.copalpite.dto.PalpiteRespostaDTO;
import com.copalpite.service.palpite.PalpiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/palpites")
@RequiredArgsConstructor
public class PalpiteController {

    private final PalpiteService palpiteService;

    @PostMapping
    public ResponseEntity<PalpiteRespostaDTO> salvar(@Valid @RequestBody PalpiteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(palpiteService.salvarOuAtualizar(dto));
    }

    @GetMapping("/bolao/{bolaoId}/usuario/{usuarioId}")
    public ResponseEntity<List<PalpiteRespostaDTO>> listarPorBolaoEUsuario(
            @PathVariable Long bolaoId,
            @PathVariable Long usuarioId) {
        return ResponseEntity.ok(palpiteService.listarPorBolaoEUsuario(bolaoId, usuarioId));
    }

    @GetMapping("/bolao/{bolaoId}/jogo/{jogoId}")
    public ResponseEntity<List<PalpiteRespostaDTO>> listarPorBolaoEJogo(
            @PathVariable Long bolaoId,
            @PathVariable Long jogoId) {
        return ResponseEntity.ok(palpiteService.listarPorBolaoEJogo(bolaoId, jogoId));
    }
}
