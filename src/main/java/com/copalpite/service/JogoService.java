package com.copalpite.service;

import com.copalpite.entity.Jogo;
import com.copalpite.repository.JogoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JogoService {

    private final JogoRepository repository;

    public List<Jogo> listarTodos() {
        return repository.findAll();
    }

    public Jogo buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Jogo não encontrado"));
    }

    public Jogo salvar(Jogo jogo) {
        return repository.save(jogo);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}