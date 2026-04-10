package com.uisil.restaurante.restaurante_pro_backend.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UsuarioDto", description = "Datos publicos del usuario")
public class UsuarioDto {
    @Schema(description = "Identificador del usuario", example = "1")
    private Long id;
    @Schema(description = "Username unico", example = "admin")
    private String username;
    @Schema(description = "Nombre para mostrar", example = "Administrador")
    private String nombre;
    @Schema(description = "Correo del usuario", example = "admin@restaurante.com")
    private String email;
    @Schema(description = "Rol principal", example = "ROLE_ADMIN")
    private String role;

    public UsuarioDto() {}

    public UsuarioDto(Long id, String username, String nombre, String email, String role) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}