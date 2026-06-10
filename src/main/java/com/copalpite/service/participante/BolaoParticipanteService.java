package com.copalpite.service.participante;

import com.copalpite.dto.BolaoRespostaDTO;
import com.copalpite.dto.EntradaBolaoDTO;
import com.copalpite.dto.ParticipanteRespostaDTO;
import java.util.List;

public interface BolaoParticipanteService {
    ParticipanteRespostaDTO entrarNoBolao(EntradaBolaoDTO dto);
    void sairDoBolao(Long bolaoId, Long usuarioId);
    List<ParticipanteRespostaDTO> listarParticipantes(Long bolaoId);
    List<ParticipanteRespostaDTO> rankingDoBolao(Long bolaoId);
    List<BolaoRespostaDTO> meusBoloes(Long usuarioId);
}
