package com.br.univille.Libranos.exception;

public class ProgressoDuplicadoException extends RuntimeException {
    public ProgressoDuplicadoException(Integer aulaId) {
        super("Aula " + aulaId + " já foi concluída por este aluno.");
    }
}
