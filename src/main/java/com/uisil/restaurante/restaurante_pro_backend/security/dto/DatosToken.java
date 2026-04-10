package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DatosToken", description = "Respuesta de autenticacion")
public record DatosToken(
        @Schema(description = "Token JWT para autenticar peticiones", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token
) {
}
