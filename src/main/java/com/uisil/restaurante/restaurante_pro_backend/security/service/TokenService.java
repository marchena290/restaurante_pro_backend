package com.uisil.restaurante.restaurante_pro_backend.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String secret;

    private static final String ISSUER = "restaurante-pro-backend";
    private static final String ROLES_CLAIM = "roles"; // Clave para guardar los roles

    /**
     * Genera un token JWT para el usuario proporcionado.
     * Incluye ID, username, roles y fecha de expiración.
     */
    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // 1. EXTRAER ROLES DEL USUARIO Y CONVERTIR A UNA CADENA SEPARADA POR COMAS
            String roles = usuario.getRoles().stream()
                    // Asegúrate de usar .toString() para el NombreRol
                    .map(rol -> rol.getNombreRol().toString())
                    .collect(Collectors.joining(","));

            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(usuario.getUsername())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    // 2. AÑADIR LOS ROLES COMO UN CLAIM EN EL TOKEN
                    .withClaim(ROLES_CLAIM, roles)
                    .sign(algorithm);

        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el token JWT", exception);
        }
    }

    /**
     * Valida el token y devuelve el Subject (username) si es válido.
     */
    public String getSubject(String token) {
        if (token == null) {
            throw new IllegalArgumentException("El token no puede ser nulo");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            return null;
        }
    }

    private Instant generarFechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }
}
