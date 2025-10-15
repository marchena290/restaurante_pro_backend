package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.Cliente;
import com.uisil.restaurante.restaurante_pro_backend.model.Reserva;
import com.uisil.restaurante.restaurante_pro_backend.repository.ClienteRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.ReservaRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements IReservaService{
     private final ReservaRepository reservaRepository;
     private final ClienteRepository clienteRepository;

    @Override
    public Reserva crearReservacion(Reserva crearReservacion) {

        // Verificar si el cliente

        // POO: extraer el ID del Cliente.


        // Validacion de tiempo
        // Obtenemos la hora de la reserva.


        // Verificar cantidad minima de personas
        // Obtenemos la cantidad de personas.



        return reservaRepository.save(crearReservacion);
    }

    @Override
    public List<Reserva> obtenerTodasLasReservaciones() {
        return reservaRepository.findAll();
    }

    @Override
    public Optional<Reserva> obtenerReservacionPorId(Long reservacionId) {
        return Optional.empty();
    }

    @Override
    public Reserva actualizarReservacion(Long reservacionId, Reserva actualizarReservacion) {
        return null;
    }

    @Override
    public void eliminarReservacion(Long reservacionId) {

    }
}
