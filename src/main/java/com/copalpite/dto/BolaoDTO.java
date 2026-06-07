package com.copalpite.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BolaoDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private Long donoId;
}