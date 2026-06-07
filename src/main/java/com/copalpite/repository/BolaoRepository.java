package com.copalpite.repository;

import com.copalpite.entity.Bolao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BolaoRepository extends JpaRepository<Bolao, Long> {
    Optional<Bolao> findByCodigoConvite(String codigoConvite);
}