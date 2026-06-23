package com.br.univille.Libranos;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

    @Test
    void gerarSenhaBCrypt() {
        String senha = "123456";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hash = encoder.encode(senha);

        System.out.println("Senha criptografada: " + hash);
    }
}