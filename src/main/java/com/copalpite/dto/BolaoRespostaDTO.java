package com.copalpite.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class BolaoRespostaDTO {
    private Long id;
    private String nome;
    private String codigoConvite;
    private String donoUsername;
    private int totalParticipantes;
    private LocalDateTime criadoEm;
}