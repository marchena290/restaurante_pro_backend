package com.uisil.restaurante.restaurante_pro_backend.exception;

public class NombreDuplicadoException extends RuntimeException{
    public NombreDuplicadoException(String mensaje){
        super(mensaje);
    }
}
