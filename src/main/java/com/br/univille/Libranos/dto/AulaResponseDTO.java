package com.br.univille.Libranos.dto;

import java.time.LocalDateTime;

public record AulaResponseDTO(
        Integer id,
        String titulo,
        String descricao,
        String videoUrl,
        Integer ordem,
        Boolean ativo,
        Integer moduloId,
        String moduloTitulo,
        LocalDateTime createdAt
) {}
