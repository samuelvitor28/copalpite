package com.copalpite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingItemDTO {
    private Long usuarioId;
    private String username;
    private Long pontos;
}