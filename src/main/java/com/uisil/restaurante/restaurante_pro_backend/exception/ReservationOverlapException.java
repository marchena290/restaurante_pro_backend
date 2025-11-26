package com.uisil.restaurante.restaurante_pro_backend.exception;

public class ReservationOverlapException extends RuntimeException {
    public ReservationOverlapException(String message) {
        super(message);
    }
    public ReservationOverlapException(String message, Throwable cause) {
        super(message, cause);
    }
}
