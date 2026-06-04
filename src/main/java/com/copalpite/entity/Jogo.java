package com.copalpite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "jogos")
@Getter @Setter @NoArgsConstructor
public class Jogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "selecao_casa_id")
    private Selecao selecaoCasa;

    @ManyToOne
    @JoinColumn(name = "selecao_visitante_id")
    private Selecao selecaoVisitante;

    private Integer golsCasa;
    private Integer golsVisitante;

    private LocalDateTime dataHora;

    private String status;

    @ManyToOne
    @JoinColumn(name = "rodada_id")
    private Rodada rodada;
}