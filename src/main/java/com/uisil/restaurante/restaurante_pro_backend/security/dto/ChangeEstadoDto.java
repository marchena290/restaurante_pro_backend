package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ChangeEstadoDto", description = "Payload para cambiar el estado de una reserva")
public class ChangeEstadoDto {
    @Schema(description = "Nuevo estado de la reserva", example = "EN_CURSO")
    public String estado;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "Fecha de confirmacion opcional", example = "2026-04-10T19:55:00")
    public LocalDateTime fechaConfirmacion;
}
