package com.copalpite.repository;

import com.copalpite.entity.Palpite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PalpiteRepository extends JpaRepository<Palpite, Long> {
    Optional<Palpite> findByUsuarioIdAndJogoIdAndBolaoId(Long usuarioId, Long jogoId, Long bolaoId);
    List<Palpite> findByBolaoIdAndUsuarioId(Long bolaoId, Long usuarioId);
    List<Palpite> findByBolaoIdAndJogoId(Long bolaoId, Long jogoId);
    List<Palpite> findByJogoId(Long jogoId);
    @Query("SELECT p.usuario.id, p.usuario.username, SUM(p.pontos) as total " +
            "FROM Palpite p WHERE p.pontos IS NOT NULL " +
            "GROUP BY p.usuario.id, p.usuario.username " +
            "ORDER BY total DESC")
    List<Object[]> rankingGeral();
}