package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DatosLogin(
       @NotBlank(message = "El nombre de usuario es obligatorio")
       @Size(min = 4, max = 50, message = "El usuario debe tener entre 4 y 50 caracteres.")
       String username,

       @NotBlank(message = "La contraseña es obligatoria")
       @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres.")
       String password
) { }
