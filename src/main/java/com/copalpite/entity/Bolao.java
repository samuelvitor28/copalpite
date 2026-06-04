package com.copalpite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bolaos")
@Getter @Setter @NoArgsConstructor
public class Bolao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true)
    private String codigoConvite;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario dono;

    @OneToMany(mappedBy = "bolao", cascade = CascadeType.ALL)
    private List<BolaoParticipante> participantes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime criadoEm;
}