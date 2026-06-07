package com.copalpite.service;

import com.copalpite.entity.Selecao;
import com.copalpite.repository.SelecaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SelecaoService {

    private final SelecaoRepository selecaoRepository;

    public List<Selecao> listarTodas() {
        return selecaoRepository.findAllByOrderByNomeAsc();
    }

    public Selecao buscarPorId(Long id) {
        return selecaoRepository.findById(id).orElseThrow(() -> new RuntimeException("Seleção não encontrada"));
    }

    public Selecao salvar(Selecao selecao) {
        return selecaoRepository.save(selecao);
    }

    public void deletar(Long id) {
        selecaoRepository.deleteById(id);
    }
}