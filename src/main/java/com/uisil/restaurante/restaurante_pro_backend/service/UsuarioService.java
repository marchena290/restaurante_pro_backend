package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.model.security.Usuario;
import com.uisil.restaurante.restaurante_pro_backend.repository.security.UsuarioRepository;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.UsuarioDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public List<UsuarioDto> list() {
        return repo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Optional<UsuarioDto> findById(Long id) {
        return repo.findById(id).map(this::toDto);
    }

    @Transactional
    public UsuarioDto update(Long id, UsuarioDto dto) {
        Usuario u = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        if (dto.getNombre() != null) u.setUsername(dto.getNombre());
        if (dto.getEmail() != null) u.setEmail(dto.getEmail());
        // No cambiar username/password/roles sin validaciones adicionales
        Usuario saved = repo.save(u);
        return toDto(saved);
    }

    private UsuarioDto toDto(Usuario u) {
        String role = null;
        if (u.getRoles() != null && !u.getRoles().isEmpty()) {
            // Ajusta según tu entidad Rol. Aquí uso getNombreRol() como ejemplo.
            try {
                role = u.getRoles().iterator().next().getNombreRol().toString();
            } catch (Exception ignored) { role = null; }
        }
        return new UsuarioDto(u.getId(), u.getUsername(), u.getNombre(), u.getEmail(), role);
    }
}
