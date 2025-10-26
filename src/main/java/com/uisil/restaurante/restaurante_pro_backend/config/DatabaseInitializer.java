package com.uisil.restaurante.restaurante_pro_backend.config;

import com.uisil.restaurante.restaurante_pro_backend.model.security.NombreRol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Rol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.RolRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer (UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void run(String... args) throws Exception {

        if (rolRepository.count() == 0){
            Rol adminRol = new Rol(null, NombreRol.ROLE_ADMIN);
            Rol userRol = new Rol(null, NombreRol.ROLE_EMPLEADO);

            rolRepository.save(adminRol);
            rolRepository.save(userRol);

            System.out.println("Roles de seguridad inicializados.");
        }
        if (usuarioRepository.findByUsername("admin").isEmpty()){

            // 1. OBTENER EL ROL DE LA BASE DE DATOS
            Rol adminRol = rolRepository.findByNombreRol(NombreRol.ROLE_ADMIN).
                    orElseThrow(() -> new RuntimeException("El rol ADMIN no existe"));
            Usuario admin = new Usuario();
            admin.setUsername("admin");

            // 2. HASHEAR LA CONTRASEÑA ANTES DE GUARDARLA;

            admin.setPassword(passwordEncoder.encode("123456"));

            admin.setRoles(Set.of(adminRol));

            usuarioRepository.save(admin);

            System.out.println("Usuario de prueba 'admin' creado exitosamente.");
        }
    }
}
