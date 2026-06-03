package com.br.univille.Libranos.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AtividadeResponseDTO(
        Integer id,
        String titulo,
        String descricao,
        String midiaUrl,
        Integer ordem,
        Boolean ativo,
        Integer aulaId,
        String aulaTitulo,
        List<AlternativaResponseDTO> alternativas,
        LocalDateTime createdAt
) {}
