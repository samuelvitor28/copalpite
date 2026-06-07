package com.copalpite.repository;

import com.copalpite.entity.Selecao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelecaoRepository extends JpaRepository<Selecao, Long> {
    List<Selecao>findAllByOrderByNomeAsc();
}