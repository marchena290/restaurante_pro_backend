package com.uisil.restaurante.restaurante_pro_backend.config;

import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;
import com.uisil.restaurante.restaurante_pro_backend.model.MesaEstado;
import com.uisil.restaurante.restaurante_pro_backend.model.security.NombreRol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Rol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import com.uisil.restaurante.restaurante_pro_backend.repository.MesaRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.RolRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.UsuarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final MesaRepository mesaRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer (UsuarioRepository usuarioRepository, RolRepository rolRepository, MesaRepository mesaRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.mesaRepository = mesaRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void run(String... args) throws Exception {
        Rol adminRol = rolRepository.findByNombreRol(NombreRol.ROLE_ADMIN)
                .orElseGet(() -> rolRepository.save(new Rol(null, NombreRol.ROLE_ADMIN)));

        rolRepository.findByNombreRol(NombreRol.ROLE_EMPLEADO)
            .orElseGet(() -> rolRepository.save(new Rol(null, NombreRol.ROLE_EMPLEADO)));

        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setRoles(Set.of(adminRol));
            usuarioRepository.save(admin);

            System.out.println("Usuario de prueba 'admin' creado exitosamente.");
        }

        if (mesaRepository.count() == 0) {
            Set<Integer> numerosUsados = new HashSet<>();
            for (int i = 0; i < 12; i++) {
                int numeroMesa;
                do {
                    numeroMesa = ThreadLocalRandom.current().nextInt(1, 51);
                } while (!numerosUsados.add(numeroMesa));

                Mesa mesa = new Mesa();
                mesa.setNumeroMesa(numeroMesa);
                mesa.setCapacidad(ThreadLocalRandom.current().nextInt(2, 11));
                mesa.setEstado(MesaEstado.DISPONIBLE);

                mesaRepository.save(mesa);
            }

            System.out.println("Mesas de prueba inicializadas.");
        }
    }
}
