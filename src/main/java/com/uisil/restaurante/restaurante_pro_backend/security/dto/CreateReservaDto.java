package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CreateReservaDto {
    @NotNull
    public Long clienteId;

    @NotNull
    public Long mesaId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime fechaHoraInicio;

    public Integer duracionMinutos;
    public Integer cantidadPersonas;
    public String nota;
    public String estado;
}
