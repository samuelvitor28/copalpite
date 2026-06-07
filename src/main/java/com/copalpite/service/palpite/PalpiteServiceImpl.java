package com.copalpite.service.palpite;

import com.copalpite.dto.PalpiteDTO;
import com.copalpite.dto.PalpiteRespostaDTO;
import com.copalpite.entity.*;
import com.copalpite.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PalpiteServiceImpl implements PalpiteService {

    private final PalpiteRepository palpiteRepository;
    private final UsuarioRepository usuarioRepository;
    private final JogoRepository jogoRepository;
    private final BolaoRepository bolaoRepository;
    private final BolaoParticipanteRepository participanteRepository;

    @Override
    @Transactional
    public PalpiteRespostaDTO salvarOuAtualizar(PalpiteDTO dto) {
        boolean participa = participanteRepository
                .existsByBolaoIdAndUsuarioId(dto.getBolaoId(), dto.getUsuarioId());
        if (!participa) {
            throw new IllegalArgumentException("Usuário não é participante deste bolão");
        }

        Palpite palpite = palpiteRepository
                .findByUsuarioIdAndJogoIdAndBolaoId(dto.getUsuarioId(), dto.getJogoId(), dto.getBolaoId())
                .orElse(new Palpite());

        if (palpite.getId() == null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
            Jogo jogo = jogoRepository.findById(dto.getJogoId())
                    .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));
            Bolao bolao = bolaoRepository.findById(dto.getBolaoId())
                    .orElseThrow(() -> new EntityNotFoundException("Bolão não encontrado"));

            palpite.setUsuario(usuario);
            palpite.setJogo(jogo);
            palpite.setBolao(bolao);
        }

        palpite.setGolsCasa(dto.getGolsCasa());
        palpite.setGolsVisitante(dto.getGolsVisitante());

        return toDTO(palpiteRepository.save(palpite));
    }

    @Override
    public List<PalpiteRespostaDTO> listarPorBolaoEUsuario(Long bolaoId, Long usuarioId) {
        return palpiteRepository.findByBolaoIdAndUsuarioId(bolaoId, usuarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PalpiteRespostaDTO> listarPorBolaoEJogo(Long bolaoId, Long jogoId) {
        return palpiteRepository.findByBolaoIdAndJogoId(bolaoId, jogoId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void recalcularPontuacao(Long jogoId) {
        Jogo jogo = jogoRepository.findById(jogoId)
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado"));

        // Só calcula se o jogo tiver resultado registrado
        if (jogo.getGolsCasa() == null || jogo.getGolsVisitante() == null) return;

        List<Palpite> palpites = palpiteRepository.findByJogoId(jogoId);

        for (Palpite palpite : palpites) {
            int pontos = calcularPontos(
                    palpite.getGolsCasa(), palpite.getGolsVisitante(),
                    jogo.getGolsCasa(), jogo.getGolsVisitante()
            );
            palpite.setPontos(pontos);
            palpiteRepository.save(palpite);

            participanteRepository
                    .findByBolaoIdAndUsuarioId(palpite.getBolao().getId(), palpite.getUsuario().getId())
                    .ifPresent(participante -> {
                        Integer totalAtual = participante.getPontuacao() != null ? participante.getPontuacao() : 0;
                        participante.setPontuacao(totalAtual + pontos);
                        participanteRepository.save(participante);
                    });
        }
    }

    // acertou placar exato = 3 pontos, acertou o vencedor somente = 1 ponto, errou os dois = 0.
    private int calcularPontos(int golsCasaPalpite, int golsVisitantePalpite, int golsCasaReal, int golsVisitanteReal) {

        int pontos = 0;
        if (golsCasaPalpite == golsCasaReal && golsVisitantePalpite == golsVisitanteReal) {
            pontos = 3;
        }

        int sinalPalpite = Integer.compare(golsCasaPalpite, golsVisitantePalpite);
        int sinalReal = Integer.compare(golsCasaReal, golsVisitanteReal);
        if (sinalPalpite == sinalReal) {
            pontos = 1;
        }

        return pontos;
    }

    private PalpiteRespostaDTO toDTO(Palpite palpite) {
        PalpiteRespostaDTO dto = new PalpiteRespostaDTO();
        dto.setId(palpite.getId());
        dto.setUsernameUsuario(palpite.getUsuario().getUsername());
        dto.setJogoDescricao(
                palpite.getJogo().getSelecaoCasa().getNome()
                        + " vs "
                        + palpite.getJogo().getSelecaoVisitante().getNome()
        );
        dto.setGolsCasa(palpite.getGolsCasa());
        dto.setGolsVisitante(palpite.getGolsVisitante());
        dto.setPontos(palpite.getPontos());
        dto.setCriadoEm(palpite.getCriadoEm());
        return dto;
    }
}
