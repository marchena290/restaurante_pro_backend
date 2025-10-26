package com.uisil.restaurante.restaurante_pro_backend.config;

import com.uisil.restaurante.restaurante_pro_backend.security.filter.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    // Inyectamos nuestro filtro personalizado
    private final SecurityFilter securityFilter;

    /**
     * Define la cadena de filtros de seguridad HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                // 1. Deshabilita CSRF (Cross-Site Request Forgery) porque usamos JWT, no sesiones.
                .csrf(csrf -> csrf.disable())

                // 2. Configura la política de sesión como STATELY (sin estado)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Define las reglas de autorización de las peticiones
                .authorizeHttpRequests(auth -> auth

                        // Permite el acceso a la ruta de login sin autenticación
                        .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()

                        // Permite el acceso a la ruta de registro sin autenticación (si la tienes)
                        .requestMatchers(HttpMethod.POST,"/api/auth/register").permitAll()

                        // Cualquier otra petición (ej. /api/pedidos, /api/usuarios) requiere autenticación
                        .anyRequest().authenticated()

                )
                // 4. Añade nuestro filtro JWT ANTES del filtro de autenticación estándar de Spring
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Bean necesario para el proceso de login (usado por el AuthenticationController).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws  Exception{
        // Spring Security configura automáticamente el gestor de autenticación
        return configuration.getAuthenticationManager();
    }

    /**
     * Bean para el algoritmo de hasheo de contraseñas.
     * Es crucial usar BCrypt para almacenar passwords de forma segura.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
