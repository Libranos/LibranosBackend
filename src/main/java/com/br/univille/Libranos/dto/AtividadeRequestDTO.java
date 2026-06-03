package com.br.univille.Libranos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AtividadeRequestDTO(
        @NotBlank(message = "Título é obrigatório") String titulo,
        String descricao,
        String midiaUrl,
        @NotNull(message = "Ordem é obrigatória") @Positive(message = "Ordem deve ser positiva") Integer ordem,
        @NotNull(message = "Aula é obrigatória") Integer aulaId,
        @NotNull @Size(min = 2, max = 5, message = "A atividade deve ter entre 2 e 5 alternativas")
        List<@Valid AlternativaRequestDTO> alternativas
) {}
