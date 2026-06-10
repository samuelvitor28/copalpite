package com.copalpite.service.participante;

import com.copalpite.dto.BolaoRespostaDTO;
import com.copalpite.dto.EntradaBolaoDTO;
import com.copalpite.dto.ParticipanteRespostaDTO;
import com.copalpite.entity.Bolao;
import com.copalpite.entity.BolaoParticipante;
import com.copalpite.entity.Usuario;
import com.copalpite.repository.BolaoParticipanteRepository;
import com.copalpite.repository.BolaoRepository;
import com.copalpite.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BolaoParticipanteServiceImpl implements BolaoParticipanteService {

    private final BolaoParticipanteRepository participanteRepository;
    private final BolaoRepository bolaoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ParticipanteRespostaDTO entrarNoBolao(EntradaBolaoDTO dto) {
        Bolao bolao = bolaoRepository.findByCodigoConvite(dto.getCodigoConvite())
                .orElseThrow(() -> new EntityNotFoundException("Código de convite inválido"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        boolean jaParticipa = participanteRepository
                .existsByBolaoIdAndUsuarioId(bolao.getId(), usuario.getId());
        if (jaParticipa) {
            throw new IllegalArgumentException("Usuário já participa deste bolão");
        }

        BolaoParticipante participante = new BolaoParticipante();
        participante.setBolao(bolao);
        participante.setUsuario(usuario);
        participante.setPontuacao(0);

        return toDTO(participanteRepository.save(participante));
    }

    @Override
    @Transactional
    public void sairDoBolao(Long bolaoId, Long usuarioId) {
        BolaoParticipante participante = participanteRepository
                .findByBolaoIdAndUsuarioId(bolaoId, usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Participante não encontrado neste bolão"));
        participanteRepository.delete(participante);
    }

    @Override
    public List<ParticipanteRespostaDTO> listarParticipantes(Long bolaoId) {
        return participanteRepository.findByBolaoId(bolaoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipanteRespostaDTO> rankingDoBolao(Long bolaoId) {
        return participanteRepository.findByBolaoId(bolaoId).stream()
                .sorted(Comparator.comparingInt(BolaoParticipante::getPontuacao).reversed())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ParticipanteRespostaDTO toDTO(BolaoParticipante p) {
        ParticipanteRespostaDTO dto = new ParticipanteRespostaDTO();
        dto.setId(p.getUsuario().getId());
        dto.setUsername(p.getUsuario().getUsername());
        dto.setNome(p.getUsuario().getNome());
        dto.setPontuacao(p.getPontuacao());
        dto.setEntradaEm(p.getEntradaEm());
        return dto;
    }

    @Override
    public List<BolaoRespostaDTO> meusBoloes(Long usuarioId) {
        return participanteRepository.findByUsuarioId(usuarioId).stream()
                .map(p -> {
                    Bolao b = p.getBolao();
                    BolaoRespostaDTO dto = new BolaoRespostaDTO();
                    dto.setId(b.getId());
                    dto.setNome(b.getNome());
                    dto.setCodigoConvite(b.getCodigoConvite());
                    dto.setDonoUsername(b.getDono().getUsername());
                    dto.setTotalParticipantes(b.getParticipantes() != null ? b.getParticipantes().size() : 0);
                    dto.setCriadoEm(b.getCriadoEm());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
