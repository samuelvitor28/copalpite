package com.copalpite.controller;

import com.copalpite.entity.Jogo;
import com.copalpite.service.JogoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jogos")
@RequiredArgsConstructor
public class JogoController {

    private final JogoService jogoService;

    @GetMapping
    public ResponseEntity<List<Jogo>> listarTodos(){
        return ResponseEntity.ok(jogoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Jogo> buscarPorId(@PathVariable Long id){
        return ResponseEntity.ok(jogoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Jogo> salvar(@RequestBody Jogo jogo){
        return ResponseEntity.status(HttpStatus.CREATED).body(jogoService.salvar(jogo));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        jogoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
