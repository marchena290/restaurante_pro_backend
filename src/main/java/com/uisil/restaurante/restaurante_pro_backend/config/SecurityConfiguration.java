package com.uisil.restaurante.restaurante_pro_backend.config;

import com.uisil.restaurante.restaurante_pro_backend.security.filter.SecurityFilter;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of(
        "http://localhost:4200",
        "http://127.0.0.1:4200",
        "https://restaurante-pro.vercel.app"
    ));
        config.setAllowCredentials(true);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // usa el source configurado
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // permitir preflight OPTIONS sin autenticación
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Lectura restringida a roles invitados/autenticados
                        .requestMatchers(HttpMethod.GET, "/api/platos/**", "/api/mesas/**", "/api/reservas/**")
                        .hasAnyRole("GUEST", "EMPLEADO", "ADMIN")

                        // Escrituras en platos y mesas: solo ADMIN
                        .requestMatchers(HttpMethod.POST, "/api/platos/**", "/api/mesas/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/platos/**", "/api/mesas/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/platos/**", "/api/mesas/**")
                        .hasRole("ADMIN")

                        // Escrituras en reservas: ADMIN o EMPLEADO
                        .requestMatchers(HttpMethod.POST, "/api/reservas/*/estado", "/api/reservas/*/checkin")
                        .hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.POST, "/api/reservas/**")
                        .hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.PUT, "/api/reservas/**")
                        .hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.PATCH, "/api/reservas/**")
                        .hasAnyRole("ADMIN", "EMPLEADO")
                        .requestMatchers(HttpMethod.DELETE, "/api/reservas/**")
                        .hasAnyRole("ADMIN", "EMPLEADO")

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
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
