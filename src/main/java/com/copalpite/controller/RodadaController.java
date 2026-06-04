package com.copalpite.controller;

import com.copalpite.entity.Rodada;
import com.copalpite.service.RodadaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rodadas")
@RequiredArgsConstructor
public class RodadaController {

    private final RodadaService rodadaService;

    @GetMapping
    public ResponseEntity<List<Rodada>> listar() {
        return ResponseEntity.ok(rodadaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rodada> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rodadaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Rodada> salvar(@RequestBody Rodada rodada) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rodadaService.salvar(rodada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        rodadaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}