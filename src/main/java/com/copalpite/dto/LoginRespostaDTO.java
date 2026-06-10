package com.copalpite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRespostaDTO {
    private String token;
    private Long id;
    private String username;
    private String nome;
}