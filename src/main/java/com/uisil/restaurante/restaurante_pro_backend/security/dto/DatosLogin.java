package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "DatosLogin", description = "Credenciales para autenticacion")
public record DatosLogin(
       @NotBlank(message = "El nombre de usuario es obligatorio")
       @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres.")
       @Schema(description = "Nombre de usuario", example = "admin")
       String username,

       @NotBlank(message = "La contraseña es obligatoria")
       @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres.")
       @Schema(description = "Contrasena del usuario", example = "Admin123")
       String password
) { }
