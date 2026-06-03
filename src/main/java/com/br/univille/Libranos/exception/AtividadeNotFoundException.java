package com.br.univille.Libranos.exception;

public class AtividadeNotFoundException extends RuntimeException {
    public AtividadeNotFoundException(Integer id) {
        super("Atividade não encontrada com id: " + id);
    }
}
