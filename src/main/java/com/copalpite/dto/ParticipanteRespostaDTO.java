package com.copalpite.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class ParticipanteRespostaDTO {
    private Long id;
    private String username;
    private String nome;
    private Integer pontuacao;
    private LocalDateTime entradaEm;
}