package com.uisil.restaurante.restaurante_pro_backend.repository;

import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MesaRepository extends JpaRepository<Mesa, Long > {

    Optional<Mesa> findByNumeroMesa(Integer numeroMesa);
}
