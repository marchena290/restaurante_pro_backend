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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

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

                logger.debug("SecurityFilter: username extraído del token = {}", username);

                // 3. Buscar el usuario en la base de datos y autenticar si existe
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
                    logger.debug("SecurityFilter: usuario encontrado en BD? {}", usuarioOpt.isPresent());

                    if (usuarioOpt.isEmpty()) {
                        // En vez de lanzar excepción que provoca 500, devolver 401 y registrar el evento
                        logger.warn("Usuario no encontrado para el token validado. username={}", username);
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado para el token validado.");
                        return;
                    }

                    UserDetails usuario = usuarioOpt.get();

                    // 4. Forzar la autenticación de Spring Security para esta petición
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            usuario, // El principal
                            null,    // Las credenciales (ya no son necesarias)
                            usuario.getAuthorities() // Los roles o authorities
                    );

                    // 5. Establecer la autenticación en el contexto de seguridad de Spring
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } catch (Exception e) {
                // Manejar la excepción (ej. token expirado, token inválido)
                if (e instanceof JWTVerificationException) {
                    logger.warn("Token inválido o expirado: {}", e.getMessage());
                    try {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
                    } catch (IOException ioe) {
                        logger.error("Error enviando respuesta 401: {}", ioe.getMessage(), ioe);
                    }
                    return;
                }

                // Para otros errores logueamos y continuamos sin autenticar
                logger.error("Error en la validación del JWT: {}", e.getMessage(), e);
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
