package com.br.univille.Libranos.dto;

import java.time.LocalDateTime;

public record ModuloResponseDTO(
        Integer id,
        String titulo,
        String descricao,
        Integer ordem,
        Boolean ativo,
        LocalDateTime createdAt
) {}
