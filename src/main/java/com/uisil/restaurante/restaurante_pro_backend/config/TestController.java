package com.uisil.restaurante.restaurante_pro_backend.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/publico")
    public String accesoPublico() {
        return "Este contenido es para TODOS (Público).";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Solo usuarios con el rol ADMIN
    public String accesoAdmin() {
        return "Este contenido es solo para ADMINISTRADORES.";
    }

    @GetMapping("/autenticado")
    @PreAuthorize("isAuthenticated()") // Solo cualquier usuario logueado (USER o ADMIN)
    public String accesoAutenticado() {
        return "Este contenido es para cualquier usuario autenticado.";
    }
}

