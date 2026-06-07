package com.copalpite.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class PalpiteRespostaDTO {
    private Long id;
    private String usernameUsuario;
    private String jogoDescricao;
    private Integer golsCasa;
    private Integer golsVisitante;
    private Integer pontos;
    private LocalDateTime criadoEm;
}
