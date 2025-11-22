package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.model.Reserva;

import java.util.List;
import java.util.Optional;

public interface IReservaService {

    // Metodos crud
    Reserva crearReservacion(Reserva crearReservacion);

    // Obtener reservaciones
    List<Reserva> obtenerTodasLasReservaciones();

    // Obtener reservacion por id
    Optional<Reserva> obtenerReservacionPorId(Long reservacionId);

    // Actualizar reversacion
    Reserva actualizarReservacion(Long reservacionId, Reserva actualizarReservacion );

    void eliminarReservacion(Long reservacionId);

    Reserva checkInReserva(Long reservaId, String performedBy);
}
