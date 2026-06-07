package com.copalpite.repository;

import com.copalpite.entity.BolaoParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BolaoParticipanteRepository extends JpaRepository<BolaoParticipante, Long> {
    List<BolaoParticipante> findByBolaoId(Long bolaoId);
    Optional<BolaoParticipante> findByBolaoIdAndUsuarioId(Long bolaoId, Long usuarioId);
    boolean existsByBolaoIdAndUsuarioId(Long bolaoId, Long usuarioId);
}
