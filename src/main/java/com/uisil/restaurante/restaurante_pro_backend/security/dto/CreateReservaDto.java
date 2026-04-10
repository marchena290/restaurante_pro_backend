package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Schema(name = "CreateReservaDto", description = "Payload para crear una reserva")
public class CreateReservaDto {
    @NotNull
    @Schema(description = "ID del cliente", example = "10")
    public Long clienteId;

    @NotNull
    @Schema(description = "ID de la mesa", example = "5")
    public Long mesaId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Fecha y hora de inicio", example = "2026-04-10T20:00:00")
    public LocalDateTime fechaHoraInicio;

    @Schema(description = "Duracion en minutos", example = "90")
    public Integer duracionMinutos;
    @Schema(description = "Cantidad de personas", example = "4")
    public Integer cantidadPersonas;
    @Schema(description = "Nota adicional", example = "Cumpleaños")
    public String nota;
    @Schema(description = "Estado inicial de la reserva", example = "PENDIENTE")
    public String estado;
}
