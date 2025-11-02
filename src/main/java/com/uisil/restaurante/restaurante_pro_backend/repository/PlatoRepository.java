package com.uisil.restaurante.restaurante_pro_backend.repository;

import com.uisil.restaurante.restaurante_pro_backend.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {

    // Ver si un plato existe
    boolean existsByNombre(String nombre);

    // Este metodo devuelve una lista de todos los platos que están marcados como 'true'
    List<Plato> findByDisponibleTrue();
}
