package com.uisil.restaurante.restaurante_pro_backend.security.dto;

public class UsuarioDto {
    private Long id;
    private String username;
    private String nombre;
    private String email;
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