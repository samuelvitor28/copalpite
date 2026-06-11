package com.copalpite.service.palpite;

import com.copalpite.dto.PalpiteDTO;
import com.copalpite.dto.PalpiteRespostaDTO;

import java.util.List;

public interface PalpiteService {
    PalpiteRespostaDTO salvarOuAtualizar(PalpiteDTO dto);
    List<PalpiteRespostaDTO> listarPorBolaoEUsuario(Long bolaoId, Long usuarioId);
    List<PalpiteRespostaDTO> listarPorBolaoEJogo(Long bolaoId, Long jogoId);
    void recalcularPontuacao(Long jogoId);
    List<PalpiteRespostaDTO> listarPorUsuario(Long usuarioId);
}
