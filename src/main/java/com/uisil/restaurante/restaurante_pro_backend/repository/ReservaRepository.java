package com.uisil.restaurante.restaurante_pro_backend.repository;

import com.uisil.restaurante.restaurante_pro_backend.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByCliente_ClienteId(Long clienteId);

    List<Reserva> findByFechahoraInicioBetween(LocalDateTime fechaHoraInicio , LocalDateTime fechaFinreal);
}
