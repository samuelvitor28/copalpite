package com.copalpite.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PalpiteDTO {
    @NotNull private Long usuarioId;
    @NotNull private Long jogoId;
    @NotNull private Long bolaoId;
    @Min(0) @NotNull private Integer golsCasa;
    @Min(0) @NotNull private Integer golsVisitante;
}