// UsuarioController.java
package com.copalpite.controller;

import com.copalpite.dto.RankingItemDTO;
import com.copalpite.dto.UsuarioCadastroDTO;
import com.copalpite.dto.UsuarioRespostaDTO;
import com.copalpite.repository.PalpiteRepository;
import com.copalpite.service.usuario.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PalpiteRepository palpiteRepository;

    @PostMapping
    public ResponseEntity<UsuarioRespostaDTO> cadastrar(@Valid @RequestBody UsuarioCadastroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioRespostaDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioRespostaDTO> buscarPorUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.buscarPorUsername(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioRespostaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioCadastroDTO dto) {
        return ResponseEntity.ok(usuarioService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingItemDTO>> ranking() {
        List<Object[]> rows = palpiteRepository.rankingGeral();
        List<RankingItemDTO> resultado = rows.stream()
                .map(r -> new RankingItemDTO((Long) r[0], (String) r[1], ((Number) r[2]).longValue()))
                .toList();
        return ResponseEntity.ok(resultado);
    }
}