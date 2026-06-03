package com.br.univille.Libranos.dto;

public record ProgressoModuloResponseDTO(
        Integer moduloId,
        String moduloTitulo,
        long totalAulas,
        long aulasConcluidadas,
        double percentualConclusao
) {}
