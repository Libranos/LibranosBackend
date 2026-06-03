package com.br.univille.Libranos.exception;

public class AulaNotFoundException extends RuntimeException {
    public AulaNotFoundException(Integer id) {
        super("Aula não encontrada com id: " + id);
    }
}
