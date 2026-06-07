package com.copalpite.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EntradaBolaoDTO {
    @NotBlank(message = "Código de convite é obrigatório")
    private String codigoConvite;
    private Long usuarioId;
}