package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DatosRegistroUsuario (

        @NotBlank
        @Size(min = 4, max = 50)
        String username,

        @NotBlank
        @Size(min = 6, max = 20)
        String password,

        @NotNull
        String rol
){ }
