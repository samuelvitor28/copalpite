package com.copalpite.entity;

import com.copalpite.enums.EGrupo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="selecoes")
@Getter @Setter @NoArgsConstructor
public class Selecao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 3)
    private String codigoSelecao;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private EGrupo grupo;

    // salvar a url da imagem da bandeira de cada país
    private String bandeira;
}
