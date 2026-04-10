package com.uisil.restaurante.restaurante_pro_backend.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Pruebas", description = "Endpoints de verificacion de seguridad")
public class TestController {

    @GetMapping("/publico")
    @Operation(summary = "Acceso publico")
    @ApiResponse(responseCode = "200", description = "Acceso permitido")
    public String accesoPublico() {
        return "Este contenido es para TODOS (Público).";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Solo usuarios con el rol ADMIN
    @Operation(summary = "Acceso solo admin")
    @ApiResponse(responseCode = "200", description = "Acceso permitido")
    public String accesoAdmin() {
        return "Este contenido es solo para ADMINISTRADORES.";
    }

    @GetMapping("/autenticado")
    @PreAuthorize("isAuthenticated()") // Solo cualquier usuario logueado (USER o ADMIN)
    @Operation(summary = "Acceso autenticado")
    @ApiResponse(responseCode = "200", description = "Acceso permitido")
    public String accesoAutenticado() {
        return "Este contenido es para cualquier usuario autenticado.";
    }
}

