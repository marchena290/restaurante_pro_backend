package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "UpdateReservaDto", description = "Payload para actualizar una reserva")
public class UpdateReservaDto {
    @Schema(description = "ID de la reserva", example = "25")
    public Long reservaId;
    @Schema(description = "ID del cliente (no editable en regla de negocio)", example = "10")
    public Long clienteId;
    @Schema(description = "ID de la nueva mesa", example = "7")
    public Long mesaId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Nueva fecha y hora de inicio", example = "2026-04-11T21:00:00")
    public LocalDateTime fechaHoraInicio;

    @Schema(description = "Nueva duracion en minutos", example = "120")
    public Integer duracionMinutos;
    @Schema(description = "Nueva cantidad de personas", example = "6")
    public Integer cantidadPersonas;
    @Schema(description = "Nueva nota", example = "Mesa cerca a la ventana")
    public String nota;
    @Schema(description = "Nuevo estado", example = "CONFIRMADO")
    public String estado;
}
