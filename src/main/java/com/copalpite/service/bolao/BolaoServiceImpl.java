package com.copalpite.service.bolao;

import com.copalpite.dto.*;
import com.copalpite.entity.Bolao;
import com.copalpite.entity.Usuario;
import com.copalpite.repository.BolaoRepository;
import com.copalpite.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BolaoServiceImpl implements BolaoService {

    private final BolaoRepository bolaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public BolaoRespostaDTO criar(BolaoDTO dto) {
        Usuario dono = usuarioRepository.findById(dto.getDonoId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        Bolao bolao = new Bolao();
        bolao.setNome(dto.getNome());
        bolao.setDono(dono);
        bolao.setCodigoConvite(gerarCodigo());

        return toDTO(bolaoRepository.save(bolao));
    }

    @Override
    public BolaoRespostaDTO buscarPorId(Long id) {
        return toDTO(encontrarPorId(id));
    }

    @Override
    public BolaoRespostaDTO buscarPorCodigoConvite(String codigo) {
        Bolao bolao = bolaoRepository.findByCodigoConvite(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Bolão não encontrado com esse código"));
        return toDTO(bolao);
    }

    @Override
    public List<BolaoRespostaDTO> listarTodos() {
        return bolaoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!bolaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Bolão não encontrado: " + id);
        }
        bolaoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public String gerarNovoCodigoConvite(Long id) {
        Bolao bolao = encontrarPorId(id);
        String novoCodigo = gerarCodigo();
        bolao.setCodigoConvite(novoCodigo);
        bolaoRepository.save(bolao);
        return novoCodigo;
    }

    private Bolao encontrarPorId(Long id) {
        return bolaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bolão não encontrado: " + id));
    }

    private String gerarCodigo() {
        // Gera um código curto tipo "A3F9B2"
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private BolaoRespostaDTO toDTO(Bolao bolao) {
        BolaoRespostaDTO dto = new BolaoRespostaDTO();
        dto.setId(bolao.getId());
        dto.setNome(bolao.getNome());
        dto.setCodigoConvite(bolao.getCodigoConvite());
        dto.setDonoUsername(bolao.getDono().getUsername());
        dto.setTotalParticipantes(
                bolao.getParticipantes() != null ? bolao.getParticipantes().size() : 0
        );
        dto.setCriadoEm(bolao.getCriadoEm());
        return dto;
    }
}