package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ChangeEstadoDto {
    public String estado;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    public LocalDateTime fechaConfirmacion;
}
