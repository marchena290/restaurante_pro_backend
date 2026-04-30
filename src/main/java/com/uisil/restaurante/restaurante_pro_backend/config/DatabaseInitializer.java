package com.uisil.restaurante.restaurante_pro_backend.config;

import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;
import com.uisil.restaurante.restaurante_pro_backend.model.MesaEstado;
import com.uisil.restaurante.restaurante_pro_backend.model.security.NombreRol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Rol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import com.uisil.restaurante.restaurante_pro_backend.repository.MesaRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.RolRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.UsuarioRepository;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.guest.username}")
    private String guestUsername;

    @Value("${app.guest.password}")
    private String guestPassword;

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

        Rol guestRol = rolRepository.findByNombreRol(NombreRol.ROLE_GUEST)
            .orElseGet(() -> rolRepository.save(new Rol(null, NombreRol.ROLE_GUEST)));

        rolRepository.findByNombreRol(NombreRol.ROLE_EMPLEADO)
            .orElseGet(() -> rolRepository.save(new Rol(null, NombreRol.ROLE_EMPLEADO)));

        Optional<Usuario> adminOptional = usuarioRepository.findByUsername(adminUsername);
        if (adminOptional.isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername(adminUsername);
            admin.setEmail("admin@restaurant.com");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRoles(Set.of(adminRol));
            usuarioRepository.save(admin);

            System.out.println("Usuario de prueba '" + adminUsername + "' creado exitosamente.");
        } else {
            Usuario adminExistente = adminOptional.get();
            boolean actualizado = false;

            if (!passwordEncoder.matches(adminPassword, adminExistente.getPassword())) {
                adminExistente.setPassword(passwordEncoder.encode(adminPassword));
                actualizado = true;
            }

            Set<Rol> rolesAdmin = adminExistente.getRoles() == null
                    ? new HashSet<>()
                    : new HashSet<>(adminExistente.getRoles());
            if (!rolesAdmin.contains(adminRol)) {
                rolesAdmin.add(adminRol);
                adminExistente.setRoles(rolesAdmin);
                actualizado = true;
            }

            if (actualizado) {
                usuarioRepository.save(adminExistente);
                System.out.println("Usuario admin sincronizado con la configuración actual.");
            }
        }

        if (usuarioRepository.findByUsername(guestUsername).isEmpty()) {
            Usuario invitado = new Usuario();
            invitado.setUsername(guestUsername);
            invitado.setNombre("INVITADO");
            invitado.setEmail("invitado@restaurant.com");
            invitado.setPassword(passwordEncoder.encode(guestPassword));
            invitado.setRoles(Set.of(guestRol));
            usuarioRepository.save(invitado);

            System.out.println("Usuario de prueba '" + guestUsername + "' creado exitosamente.");
        }

        if (mesaRepository.count() == 0) {
            String[] ubicaciones = {"Interior", "Terraza", "Patio", "Área VIP", "Barra"};
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

                // Asigna una ubicación aleatoria
                String ubicacionAleatoria = ubicaciones[ThreadLocalRandom.current().nextInt(ubicaciones.length)];
                mesa.setUbicacion(ubicacionAleatoria);

                mesaRepository.save(mesa);
            }

            System.out.println("Mesas de prueba inicializadas.");
        }
    }
}
