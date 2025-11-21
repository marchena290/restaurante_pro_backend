package com.uisil.restaurante.restaurante_pro_backend.security.service;

import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImple implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Soportar inicio de sesión por username o por email
        return usuarioRepository.findByUsernameOrEmail(username, username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
