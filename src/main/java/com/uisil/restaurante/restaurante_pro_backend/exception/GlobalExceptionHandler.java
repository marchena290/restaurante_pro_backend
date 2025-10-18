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

    // 3. NUEVO: Maneja 400 Bad Request (Petición Inválida, Choque de Horario, etc.)
    @ExceptionHandler(PeticionInvalida.class)
    public  ResponseEntity<Object> handlePeticionInvalidaException(PeticionInvalida ex){

        // Crea el cuerpo Json de la respuesta (similar al Email Duplicado)
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad request");
        // El mensaje contendrá el detalle (ej: "El horario seleccionado choca...")
        body.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // 4. NUEVO: Maneja 400 Bad Request (Nombre de Plato Duplicado)
    @ExceptionHandler(NombreDuplicadoException.class)
    public  ResponseEntity<Object> handleNombreDuplicadoException(NombreDuplicadoException ex){

        // Crea el cuerpo Json de la respuesta (similar a PeticionInvalida)
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", new Date());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad request");
        // El mensaje contendrá el detalle (ej: "El nombre del plato ya existe...")
        body.put("mensaje", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

}
