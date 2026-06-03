package com.br.univille.Libranos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AulaRequestDTO(
        @NotBlank(message = "Título é obrigatório") String titulo,
        String descricao,
        @NotNull(message = "Ordem é obrigatória") @Positive(message = "Ordem deve ser positiva") Integer ordem,
        @NotNull(message = "Módulo é obrigatório") Integer moduloId
) {}
