package com.br.univille.Libranos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlternativaRequestDTO(
        @NotBlank(message = "Texto da alternativa é obrigatório") String texto,
        String midiaUrl,
        @NotNull(message = "Campo 'correta' é obrigatório") Boolean correta
) {}
