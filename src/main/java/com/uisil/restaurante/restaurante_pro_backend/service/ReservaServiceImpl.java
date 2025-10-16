package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.exception.PeticionInvalida;
import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.Cliente;
import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;
import com.uisil.restaurante.restaurante_pro_backend.model.Reserva;
import com.uisil.restaurante.restaurante_pro_backend.repository.ClienteRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements IReservaService{
     private final ReservaRepository reservaRepository;
     private final ClienteRepository clienteRepository;

    @Override
    public Reserva crearReservacion(Reserva crearReservacion) {

        // Verifica que el cliente que pide la reserva exista.
        Long clienteId = crearReservacion.getCliente().getClienteId();
        Optional<Cliente> clienteExiste = clienteRepository.findById(clienteId);

        if (clienteExiste.isEmpty()){
            throw new RecursoNoEncontradoException("Cliente ", clienteId);
        }

        // Verifica que la reserva no sea para el pasado.
        LocalDateTime fechaHoraInicio = crearReservacion.getFechaHoraInicio();
        if (fechaHoraInicio.isBefore(LocalDateTime.now())){
            throw new RuntimeException("La reserva debe ser hecha en una fecha futura");
        }

        // Verifica que haya al menos una persona.
        Integer cantidadPersonas = crearReservacion.getCantidadPersonas();
        if (cantidadPersonas <= 0){
            throw new RuntimeException("El número minímo de personas para hacer una reserva es de 1");
        }

        // Pasamos NULL porque NO estamos excluyendo ninguna reserva (es una creación).
        validarChoqueDeHorario(crearReservacion, null);

        return reservaRepository.save(crearReservacion);
    }

    @Override
    public List<Reserva> obtenerTodasLasReservaciones() {
        return reservaRepository.findAll();
    }

    @Override
    public Optional<Reserva> obtenerReservacionPorId(Long reservacionId) {
        Reserva reservaEncontrada = reservaRepository.findById(reservacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", reservacionId));

        return  Optional.of(reservaEncontrada);
    }

    @Override
    public Reserva actualizarReservacion(Long reservacionId, Reserva actualizarReservacion) {
        return reservaRepository.findById(reservacionId)
                .map(reservaOriginal -> {

                    // 1. VALIDACIÓN: ID de Consistencia (URL vs. Cuerpo)
                    if (!reservacionId.equals(actualizarReservacion.getReservaId())){
                        throw new PeticionInvalida("El ID de la reserva en la URL no coincide con el ID enviado en el cuerpo.");
                    }

                    // 2. VALIDACIÓN CONDICIONAL: Integridad referencial del Cliente (si cambia).
                    Long idOrignal = reservaOriginal.getCliente().getClienteId();
                    Long idNuevo = actualizarReservacion.getCliente().getClienteId();

                    // Verificamos si los IDs cambiaron
                    if (!Objects.equals(idOrignal, idNuevo)){

                        Optional<Cliente> clienteEncontrado = clienteRepository.findById(idNuevo);

                        if (clienteEncontrado.isEmpty()){
                            throw new RecursoNoEncontradoException("Cliente ", idNuevo);
                        }
                        // Actualizar la referencia al cliente en el objeto original si el ID es válido.
                        reservaOriginal.setCliente(actualizarReservacion.getCliente());
                    }

                    // 3. VALIDACIÓN: La nueva fecha de inicio no debe ser en el pasado.
                    LocalDateTime fechaHoraInicio = actualizarReservacion.getFechaHoraInicio();
                    if (fechaHoraInicio.isBefore(LocalDateTime.now())){
                        throw new PeticionInvalida("La fecha y hora de inicio de la reserva no puede ser anterior a la hora actual.");
                    }

                    // 4. VALIDACIÓN: Modificación prohibida si la reserva inicia en <= 30 minutos.
                    Duration duracionHastaInicio = Duration.between(LocalDateTime.now(), fechaHoraInicio);

                    if (duracionHastaInicio.toMinutes() > 0 && duracionHastaInicio.toMinutes() <= 30){
                        throw new PeticionInvalida("No se permiten modificaciones a reservas que inician en menos de 30 minutos.");
                    }

                    // 5. VALIDACIÓN AVANZADA: Choque de Horario (Usando método compartido).
                    validarChoqueDeHorario(actualizarReservacion, reservacionId);

                    // 6. ASIGNACIÓN y PERSISTENCIA: Copiar las propiedades y guardar el objeto original.
                    reservaOriginal.setMesaId(actualizarReservacion.getMesaId());
                    reservaOriginal.setFechaConfirmacion(actualizarReservacion.getFechaConfirmacion());
                    reservaOriginal.setFechaHoraInicio(actualizarReservacion.getFechaHoraInicio());
                    reservaOriginal.setCantidadPersonas(actualizarReservacion.getCantidadPersonas());
                    reservaOriginal.setEstado(actualizarReservacion.getEstado());
                    reservaOriginal.setNota(actualizarReservacion.getNota());

                    return reservaRepository.save(reservaOriginal);
                })
                .orElseThrow(()-> new RecursoNoEncontradoException("Reserva", reservacionId));

    }

    @Override
    public void eliminarReservacion(Long reservacionId) {
        Reserva reservaEncontrada = reservaRepository.findById(reservacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva " ,reservacionId));

        reservaRepository.delete(reservaEncontrada);
    }

    private void validarChoqueDeHorario(Reserva reserva, Long idAExcluir) {
        long duracion = reserva.getDuracionMinutos();
        LocalDateTime nuevoInicio = reserva.getFechaHoraInicio();
        LocalDateTime nuevaFechaFin = nuevoInicio.plusMinutes(duracion);

        // Obtener el objeto Mesa para la busqueda
        Mesa mesavalidacion = reserva.getMesaId();

        // Ejecutar la búsqueda de posibles conflictos.
        List<Reserva> conflictos = reservaRepository.findByFechaHoraInicioBetweenAndMesaId(nuevoInicio, nuevaFechaFin, mesavalidacion);

        // Filtrar la lista de conflictos, excluyendo la reserva actual (si aplica, para PUT).
        List<Reserva> choquesFiltrado = conflictos.stream()
                // Si idAExcluir es null (caso POST), el filtro lo ignora.
                .filter(r -> idAExcluir == null || !r.getReservaId().equals(idAExcluir))
                .collect(Collectors.toList());

        // Si quedan elementos después de filtrar, existe un choque real.
        if (!choquesFiltrado.isEmpty()) {
            throw new PeticionInvalida("El horario seleccionado choca con una reserva existente para la misma mesa.");
        }
    }
}
