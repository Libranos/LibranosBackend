package com.br.univille.Libranos.dto;

public record AlternativaResponseDTO(
        Integer id,
        String texto,
        String midiaUrl,
        Boolean correta
) {}
