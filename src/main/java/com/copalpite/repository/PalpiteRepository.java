package com.copalpite.repository;

import com.copalpite.entity.Palpite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PalpiteRepository extends JpaRepository<Palpite, Long> {
}