package com.copalpite.service.usuario;

import com.copalpite.dto.UsuarioCadastroDTO;
import com.copalpite.dto.UsuarioRespostaDTO;
import java.util.List;

public interface UsuarioService {
    UsuarioRespostaDTO cadastrar(UsuarioCadastroDTO dto);
    UsuarioRespostaDTO buscarPorId(Long id);
    UsuarioRespostaDTO buscarPorUsername(String username);
    List<UsuarioRespostaDTO> listarTodos();
    UsuarioRespostaDTO atualizar(Long id, UsuarioCadastroDTO dto);
    void deletar(Long id);
}
