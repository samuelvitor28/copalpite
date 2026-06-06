package com.copalpite.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter @Setter
public class UsuarioRespostaDTO {

    private Long id;
    private String username;
    private String nome;
    private String ultimoNome;
    private String email;
    private String selecaoTorcidaNome;
    private String selecaoTorcidaBandeira;
    private LocalDateTime criadoEm;
}