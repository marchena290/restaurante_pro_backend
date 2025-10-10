package com.uisil.restaurante.restaurante_pro_backend.exception;

public class EmailDuplicadoException extends RuntimeException{
    public EmailDuplicadoException (String mensaje){
        super(mensaje);
    }
}
