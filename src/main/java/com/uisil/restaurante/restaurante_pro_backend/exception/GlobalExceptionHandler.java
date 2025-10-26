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

    /**
     * Método reutilizable para construir el cuerpo JSON estandarizado de la respuesta de error.
     */

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String error, String message){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        body.put("error", error);
        body.put("mensaje", message);
        return new ResponseEntity<>(body, status);
    }

    // 1. Maneja 404 Not found (Recurso no encontrado)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Object> handleRecursoNoEncontradoException(RecursoNoEncontradoException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    // 2. Maneja 400 Bad Request (Email Duplicado  Error de Cliente)
    @ExceptionHandler(EmailDuplicadoException.class)
    public  ResponseEntity<Object> handleEmailDuplicadoException(EmailDuplicadoException ex){
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Bad Request", ex.getMessage());
    }

    // 3. NUEVO: Maneja 400 Bad Request (Petición Inválida, Choque de Horario, etc.)
    @ExceptionHandler(PeticionInvalida.class)
    public  ResponseEntity<Object> handlePeticionInvalidaException(PeticionInvalida ex){
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // 4. NUEVO: Maneja 400 Bad Request (Nombre de Plato Duplicado)
    @ExceptionHandler(NombreDuplicadoException.class)
    public  ResponseEntity<Object> handleNombreDuplicadoException(NombreDuplicadoException ex){
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public  ResponseEntity<Object> handleGeneralException(Exception ex){
        // Para el cliente, solo enviamos un mensaje genérico por seguridad.
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Ocurrió un error inesperado en el servidor.");
    }
}
