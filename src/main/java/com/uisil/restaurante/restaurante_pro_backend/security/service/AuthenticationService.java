package com.uisil.restaurante.restaurante_pro_backend.security.service;

import com.uisil.restaurante.restaurante_pro_backend.model.security.NombreRol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Rol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.RolRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.UsuarioRepository;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosLogin;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosRegistroUsuario;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    /**
     * Proceso de autenticación de usuario y generación del token JWT.
     */
    public DatosToken login(DatosLogin request) {

        // 1. Crear el token de autenticación para el manager
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        );

        // 2. Autenticar usando el AuthenticationManager.
        // El Manager internamente llama a UserDetailsServiceImple.
        Authentication authentication = authenticationManager.authenticate(authToken);

        // 3. Obtener el usuario autenticado (Principal)
        // Se asume que el Principal es una instancia de nuestra clase Usuario.
        Usuario usuario = (Usuario) authentication.getPrincipal();

        // 4. Generar y devolver el token
        String jwtToken = tokenService.generarToken(usuario);
        return  new DatosToken(jwtToken);
    }

    /**
     * Proceso de registro de un nuevo usuario en la base de datos, incluyendo el rol.
     */
    public Usuario registroUsuario(DatosRegistroUsuario request) {
        // 1. Verificar si el nombre de usuario ya existe
        if (usuarioRepository.findByUsername(request.username()).isPresent()){
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        // 2. Encriptar la contraseña antes de guardar (¡CRÍTICO PARA LA SEGURIDAD!)
        String encodedPassword = passwordEncoder.encode(request.password());

        // 3. CONVERSIÓN: String del DTO a Enum (NombreRol)
        NombreRol rolEnum;
        try {
            rolEnum = NombreRol.valueOf(request.rol());
        } catch (IllegalArgumentException e){
            throw new RuntimeException("El rol especificado no es válido: " + request.rol());
        }

        // 4. Buscar el Rol en la base de datos
        Rol rolSeleccionado = rolRepository.findByNombreRol(rolEnum)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado en la base de datos: " + request.rol()));

        // 5. Crear la nueva entidad Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.username());
        nuevoUsuario.setPassword(encodedPassword); // <-- USAMOS LA CONTRASEÑA ENCRIPTADA

        // 6. ASIGNAR EL ROL
        nuevoUsuario.setRoles(Set.of(rolSeleccionado));

        // 7. Guardar el nuevo usuario
        return  usuarioRepository.save(nuevoUsuario);
    }
}
