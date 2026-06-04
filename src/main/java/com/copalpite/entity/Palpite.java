package com.copalpite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "palpites")
@Getter @Setter @NoArgsConstructor
public class Palpite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;

    @ManyToOne
    @JoinColumn(name = "bolao_id")
    private Bolao bolao;

    private Integer golsCasa;
    private Integer golsVisitante;

    private Integer pontos;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;
}