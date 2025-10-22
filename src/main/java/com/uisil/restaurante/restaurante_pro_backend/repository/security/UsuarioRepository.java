package com.uisil.restaurante.restaurante_pro_backend.repository.security;

import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // buscar usuario por nombre
    Optional<UserDetails> findByUsername(String username);
}
