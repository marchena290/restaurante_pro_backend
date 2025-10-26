package com.uisil.restaurante.restaurante_pro_backend.repository.security;

import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
}
