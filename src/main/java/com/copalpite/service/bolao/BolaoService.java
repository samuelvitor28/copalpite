package com.copalpite.service.bolao;

import com.copalpite.dto.*;
import java.util.List;

public interface BolaoService {
    BolaoRespostaDTO criar(BolaoDTO dto);
    BolaoRespostaDTO buscarPorId(Long id);
    BolaoRespostaDTO buscarPorCodigoConvite(String codigo);
    List<BolaoRespostaDTO> listarTodos();
    void deletar(Long id);
    String gerarNovoCodigoConvite(Long id);
}