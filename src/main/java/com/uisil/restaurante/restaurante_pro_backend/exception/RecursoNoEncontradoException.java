package com.uisil.restaurante.restaurante_pro_backend.exception;

public class RecursoNoEncontradoException extends RuntimeException{

    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + "con ID" + id + "no fue encontrado");
    }
}
