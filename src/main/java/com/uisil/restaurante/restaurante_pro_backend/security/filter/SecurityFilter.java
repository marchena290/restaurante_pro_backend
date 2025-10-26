package com.uisil.restaurante.restaurante_pro_backend.security.filter;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.UsuarioRepository;
import com.uisil.restaurante.restaurante_pro_backend.security.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    /**
     * Este método se ejecuta una vez por cada petición HTTP.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtener el token del encabezado (Header) de la petición
        String tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            try {
                // 2. Validar el token y obtener el username (Subject)
                String username = tokenService.getSubject(tokenJWT);

                // 3. Buscar el usuario en la base de datos
                UserDetails usuario = usuarioRepository.findByUsername(username)
                        .orElseThrow(() -> new ServletException("Usuario no encontrado para el token validado."));

                // 4. Forzar la autenticación de Spring Security para esta petición
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        usuario, // El principal
                        null,    // Las credenciales (ya no son necesarias)
                        usuario.getAuthorities() // Los roles o authorities
                );

                // 5. Establecer la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Manejar la excepción (ej. token expirado, token inválido)
                logger.error("Error en la validación del JWT: " + e.getMessage(), e);
                // No se detiene la cadena aquí, simplemente el SecurityContextHolder no se actualiza,
                // y la petición será manejada más adelante como "No Autorizada" (401).
            }
        }

        // Continúa la cadena de filtros de Spring Security
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del encabezado Authorization.
     */
    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // El formato esperado es "Bearer <token>"
            return authorizationHeader.replace("Bearer ", "");
        }

        // Si no hay encabezado o no tiene el formato correcto, retorna null
        return null;
    }
}
