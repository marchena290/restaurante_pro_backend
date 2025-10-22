package com.uisil.restaurante.restaurante_pro_backend.repository.security;

import com.uisil.restaurante.restaurante_pro_backend.model.security.NombreRol;
import com.uisil.restaurante.restaurante_pro_backend.model.security.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    // Método clave para el servicio de registro: busca la entidad Rol por su nombre (ej: ROLE_ADMIN)

    Optional<Rol> findByNombreRol(NombreRol nombreRol);
}
