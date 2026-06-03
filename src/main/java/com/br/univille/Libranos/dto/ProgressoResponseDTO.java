package com.br.univille.Libranos.dto;

import java.time.LocalDateTime;

public record ProgressoResponseDTO(
        Integer id,
        Integer aulaId,
        String aulaTitulo,
        Integer moduloId,
        LocalDateTime concluidoEm
) {}
