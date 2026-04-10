package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "DatosRegistroUsuario", description = "Datos para crear una nueva cuenta")
public record DatosRegistroUsuario (

        @NotBlank
        @Size(min = 4, max = 50)
        @Schema(description = "Nombre de usuario", example = "empleado01")
        String username,

        @NotBlank
        @Size(min = 6, max = 20)
        @Schema(description = "Contrasena de la cuenta", example = "ClaveSegura1")
        String password,

        @NotNull
        @Schema(description = "Rol asignado al usuario", example = "ROLE_EMPLEADO")
        String rol
){ }
