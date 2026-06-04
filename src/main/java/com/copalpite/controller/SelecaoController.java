package com.copalpite.controller;

import com.copalpite.entity.Selecao;
import com.copalpite.enums.EGrupo;
import com.copalpite.service.SelecaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/selecoes")
@RequiredArgsConstructor
public class SelecaoController {

    private final SelecaoService selecaoService;

    @GetMapping
    public ResponseEntity<List<Selecao>> listarTodas() {
        return ResponseEntity.ok(selecaoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Selecao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(selecaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Selecao> salvar(@RequestBody Selecao selecao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(selecaoService.salvar(selecao));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Selecao> atualizar(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        Selecao selecao = selecaoService.buscarPorId(id);

        if (campos.containsKey("nome")) selecao.setNome((String) campos.get("nome"));
        if (campos.containsKey("codigo")) selecao.setCodigoSelecao((String) campos.get("codigo"));
        if (campos.containsKey("bandeira")) selecao.setBandeira((String) campos.get("bandeira"));
        if (campos.containsKey("grupo")) selecao.setGrupo(EGrupo.valueOf((String) campos.get("grupo")));

        return ResponseEntity.ok(selecaoService.salvar(selecao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        selecaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}