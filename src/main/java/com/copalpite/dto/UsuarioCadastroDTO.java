package com.copalpite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioCadastroDTO {

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 6, max = 50)
    private String username;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Último nome é obrigatório")
    private String ultimoNome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    private Long selecaoTorcidaId; // opcional
}