package com.br.univille.Libranos.dto;

public record LoginResponse(
        String token,
        Long expiresIn,
        String role
) {
}
