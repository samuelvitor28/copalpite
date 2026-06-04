package com.copalpite.service;

import com.copalpite.entity.Rodada;
import com.copalpite.repository.RodadaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RodadaService {
    private final RodadaRepository repository;

    public RodadaService(RodadaRepository repository) {
        this.repository = repository;
    }

    public Rodada buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Rodada não encontrada"));
    }

    public Rodada salvar(Rodada rodada) {
        return repository.save(rodada);
    }

    public List<Rodada> listar() {
        return repository.findAll();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
