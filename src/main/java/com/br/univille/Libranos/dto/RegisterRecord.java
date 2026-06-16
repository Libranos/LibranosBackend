package com.br.univille.Libranos.dto;

import com.br.univille.Libranos.models.Role;

public record RegisterRecord(
        String email,
        String password,
        String fullName,
        Role role
) {
}
