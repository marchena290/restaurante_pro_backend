package com.uisil.restaurante.restaurante_pro_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Maneja 404 Not found (Recurso no encontrado)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Object> handleRecursoNoEncontradoException(RecursoNoEncontradoException ex) {

        // Crear el cuerpo Json de respuesta
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("mensaje", ex.getMessage());

        return  new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // 2. Maneja 400 Bad Request (Email Duplicado  Error de Cliente)
    @ExceptionHandler(EmailDuplicadoException.class)
    public  ResponseEntity<Object> handleEmailDuplicadoException(EmailDuplicadoException ex){

        // crea el cuerpo Json de la respuesta
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad request");
        body.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
