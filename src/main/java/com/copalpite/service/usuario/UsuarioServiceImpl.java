
package com.copalpite.service.usuario;

import com.copalpite.dto.UsuarioCadastroDTO;
import com.copalpite.dto.UsuarioRespostaDTO;
import com.copalpite.entity.Selecao;
import com.copalpite.entity.Usuario;
import com.copalpite.repository.SelecaoRepository;
import com.copalpite.repository.UsuarioRepository;
import com.copalpite.service.usuario.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final SelecaoRepository selecaoRepository;

    @Override
    @Transactional
    public UsuarioRespostaDTO cadastrar(UsuarioCadastroDTO dto) {
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username já está em uso");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setNome(dto.getNome());
        usuario.setUltimoNome(dto.getUltimoNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha()); // ⚠️ hash com BCrypt quando adicionar Spring Security
        usuario.setCriadoEm(LocalDateTime.now());

        if (dto.getSelecaoTorcidaId() != null) {
            Selecao selecao = selecaoRepository.findById(dto.getSelecaoTorcidaId())
                    .orElseThrow(() -> new EntityNotFoundException("Seleção não encontrada"));
            usuario.setSelecaoTorcida(selecao);
        }

        return toDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioRespostaDTO buscarPorId(Long id) {
        return toDTO(encontrarPorId(id));
    }

    @Override
    public UsuarioRespostaDTO buscarPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + username));
        return toDTO(usuario);
    }

    @Override
    public List<UsuarioRespostaDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioRespostaDTO atualizar(Long id, UsuarioCadastroDTO dto) {
        Usuario usuario = encontrarPorId(id);

        if (!usuario.getUsername().equals(dto.getUsername())
                && usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username já está em uso");
        }
        if (!usuario.getEmail().equals(dto.getEmail())
                && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        usuario.setUsername(dto.getUsername());
        usuario.setNome(dto.getNome());
        usuario.setUltimoNome(dto.getUltimoNome());
        usuario.setEmail(dto.getEmail());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(dto.getSenha());
        }

        if (dto.getSelecaoTorcidaId() != null) {
            Selecao selecao = selecaoRepository.findById(dto.getSelecaoTorcidaId())
                    .orElseThrow(() -> new EntityNotFoundException("Seleção não encontrada"));
            usuario.setSelecaoTorcida(selecao);
        } else {
            usuario.setSelecaoTorcida(null);
        }

        return toDTO(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario encontrarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + id));
    }

    private UsuarioRespostaDTO toDTO(Usuario usuario) {
        UsuarioRespostaDTO dto = new UsuarioRespostaDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setNome(usuario.getNome());
        dto.setUltimoNome(usuario.getUltimoNome());
        dto.setEmail(usuario.getEmail());
        dto.setCriadoEm(usuario.getCriadoEm());

        if (usuario.getSelecaoTorcida() != null) {
            dto.setSelecaoTorcidaNome(usuario.getSelecaoTorcida().getNome());
            dto.setSelecaoTorcidaBandeira(usuario.getSelecaoTorcida().getBandeira());
        }

        return dto;
    }
}