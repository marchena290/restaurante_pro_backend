package com.uisil.restaurante.restaurante_pro_backend.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(
        title = "Restaurante Pro API",
        version = "1.0",
        description = "Documentacón de la API REST para el sistema de gestión de restaurante Pro"
    ),
    security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Ingrese el token JWT obtenido al iniciar sesión para acceder a los endpoints protegidos"
)
public class OpenApiConfig {
    
}
