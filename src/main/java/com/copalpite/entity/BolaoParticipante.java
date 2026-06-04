package com.copalpite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "bolao_participantes")
@Getter @Setter @NoArgsConstructor
public class BolaoParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bolao_id")
    private Bolao bolao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private Integer pontuacao;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime entradaEm;
}