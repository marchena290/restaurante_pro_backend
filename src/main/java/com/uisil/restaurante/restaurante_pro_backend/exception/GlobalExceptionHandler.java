package com.uisil.restaurante.restaurante_pro_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Método reutilizable para construir el cuerpo JSON estandarizado de la respuesta de error.
     */
    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("mensaje", message);
        return new ResponseEntity<>(body, status);
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String error, String message, List<Map<String,Object>> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("mensaje", message);
        if (errors != null && !errors.isEmpty()) {
            body.put("errors", errors);
        }
        return new ResponseEntity<>(body, status);
    }

    // 1. Maneja 404 Not found (Recurso no encontrado)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Object> handleRecursoNoEncontradoException(RecursoNoEncontradoException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    // 2. Maneja 400 Bad Request (Email Duplicado  Error de Cliente)
    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<Object> handleEmailDuplicadoException(EmailDuplicadoException ex) {
        // CORRECCIÓN: devolver 400 Bad Request (no 404)
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // 3. NUEVO: Maneja 400 Bad Request (Petición Inválida, Choque de Horario, etc.)
    @ExceptionHandler(PeticionInvalida.class)
    public ResponseEntity<Object> handlePeticionInvalidaException(PeticionInvalida ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // 4. NUEVO: Maneja 400 Bad Request (Nombre de Plato Duplicado)
    @ExceptionHandler(NombreDuplicadoException.class)
    public ResponseEntity<Object> handleNombreDuplicadoException(NombreDuplicadoException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // 5. Manejo de validación Bean Validation -> devuelve 400 con detalles por campo
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
        List<Map<String, Object>> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map((FieldError fe) -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("field", fe.getField());
                    m.put("rejected", fe.getRejectedValue());
                    m.put("message", fe.getDefaultMessage());
                    return m;
                })
                .collect(Collectors.toList());

        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Bad Request", "Datos inválidos", fieldErrors);
    }

    // 6. Manejo de credenciales inválidas -> devuelve 401 con mensaje amigable
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, "Unauthorized", "Usuario o contraseña incorrectos");
    }

    // 7. Manejo general de excepciones de autenticación (por si usas otros tipos)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthException(AuthenticationException ex) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, "Unauthorized", "Usuario o contraseña incorrectos");
    }

    // 8. Handler genérico para errores inesperados (500) — registra el stacktrace en el log
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        logger.error("Error inesperado en la aplicación", ex);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Ocurrió un error inesperado en el servidor.");
    }
}
