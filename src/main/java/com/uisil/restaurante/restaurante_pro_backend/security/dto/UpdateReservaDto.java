package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class UpdateReservaDto {
    public Long reservaId;
    public Long clienteId;
    public Long mesaId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime fechaHoraInicio;

    public Integer duracionMinutos;
    public Integer cantidadPersonas;
    public String nota;
    public String estado;
}
