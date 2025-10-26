package com.uisil.restaurante.restaurante_pro_backend.security.service;


import com.uisil.restaurante.restaurante_pro_backend.model.security.NombreRol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Rol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.RolRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.UsuarioRepository;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.DatosRegistroUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Service
public class RegistroService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroService (UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrarUsuario(DatosRegistroUsuario datos){
        if (usuarioRepository.findByUsername(datos.username()).isPresent()){
            throw new RuntimeException("El nombre de usuario ya está en uso.");
        }

        NombreRol nombreRol;
        try{
            nombreRol = NombreRol.valueOf("ROLE_" + datos.rol().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol no válido: " + datos.rol() + ". Asegúrese de usar solo el nombre, ej: ADMIN, EMPLEADO, CLIENTE.");
        }

        Rol rol = rolRepository.findByNombreRol(nombreRol)
                .orElseThrow(()-> new RuntimeException("La entidad Rol '" + nombreRol.name() + "' no se encontró en la base de datos. Debe cargar los roles (ROLE_ADMIN, ROLE_EMPLEADO, etc.) al inicio."));

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(datos.username());

        String hashedPasssword = passwordEncoder.encode(datos.password());
        nuevoUsuario.setPassword(hashedPasssword);

        Set<Rol> roles = Collections.singleton(rol);
        nuevoUsuario.setRoles(roles);

        return usuarioRepository.save(nuevoUsuario);
    }
}
