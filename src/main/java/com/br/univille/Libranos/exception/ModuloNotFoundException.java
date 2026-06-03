package com.br.univille.Libranos.exception;

public class ModuloNotFoundException extends RuntimeException {
    public ModuloNotFoundException(Integer id) {
        super("Módulo não encontrado com id: " + id);
    }
}
