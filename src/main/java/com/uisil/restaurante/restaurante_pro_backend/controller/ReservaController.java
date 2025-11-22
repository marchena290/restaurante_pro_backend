package com.uisil.restaurante.restaurante_pro_backend.controller;

import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.Cliente;
import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;
import com.uisil.restaurante.restaurante_pro_backend.model.Reserva;
import com.uisil.restaurante.restaurante_pro_backend.repository.ClienteRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.MesaRepository;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.CreateReservaDto;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.UpdateReservaDto;
import com.uisil.restaurante.restaurante_pro_backend.service.IReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final IReservaService reservaService;
    private final ClienteRepository clienteRepository;
    private final MesaRepository mesaRepository;

    // Crear una reservacion
    @PostMapping
    public ResponseEntity<?> crearReservacion(@RequestBody @Valid CreateReservaDto dto){
       try {
           if(dto.clienteId == null) return ResponseEntity.badRequest().body("clienteId es requerido");
           if(dto.mesaId == null) return ResponseEntity.badRequest().body("mesaId es requerido");

           Cliente cliente = clienteRepository.findById(dto.clienteId)
                   .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", dto.clienteId));
           Mesa mesa = mesaRepository.findById(dto.mesaId)
                   .orElseThrow(() -> new RecursoNoEncontradoException("Mesa", dto.mesaId));

           Reserva r = new Reserva();
           r.setCliente(cliente);
           r.setMesaId(mesa);
           r.setFechaHoraInicio(dto.fechaHoraInicio);
           r.setDuracionMinutos(dto.duracionMinutos);
           r.setCantidadPersonas(dto.cantidadPersonas);
           r.setNota(dto.nota);
           if (dto.estado != null) r.setEstado(com.uisil.restaurante.restaurante_pro_backend.model.EstadoReserva.valueOf(dto.estado));

           Reserva creada = reservaService.crearReservacion(r);
           return new ResponseEntity<>(creada, HttpStatus.CREATED);

       } catch (RecursoNoEncontradoException ex) {
           return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       } catch (Exception ex) {
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la reservacion: " + ex.getMessage());
       }
    }

    // Mostrar todas las reservaciones
    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodasLasReservaciones() {
        List<Reserva> reservas = reservaService.obtenerTodasLasReservaciones();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{reservaId}")
    public ResponseEntity<Reserva> obtenerReservacionPorId(@PathVariable Long reservacionId){
        return reservaService.obtenerReservacionPorId(reservacionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{reservaId}")
    public  ResponseEntity<?> actualizarReservacion(@PathVariable(name = "reservaId") Long reservaId, @RequestBody @Valid UpdateReservaDto dto){
        try {
            Reserva existente = reservaService.obtenerReservacionPorId(reservaId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", reservaId));

            if(dto.clienteId != null){
                Cliente cliente = clienteRepository.findById(dto.clienteId)
                        .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", dto.clienteId));
                existente.setCliente(cliente);
            }

            if(dto.mesaId != null) {
               Mesa mesa = mesaRepository.findById(dto.mesaId)
                       .orElseThrow(()-> new RecursoNoEncontradoException("Mesa", dto.mesaId));
               existente.setMesaId(mesa);
            }

            if (dto.clienteId != null && !dto.clienteId.equals(existente.getCliente().getClienteId())) {
                return ResponseEntity.badRequest().body("No está permitido cambiar el cliente de una reserva existente.");
            }

            if(dto.fechaHoraInicio != null) existente.setFechaHoraInicio(dto.fechaHoraInicio);
            if(dto.duracionMinutos != null) existente.setDuracionMinutos(dto.duracionMinutos);
            if(dto.cantidadPersonas != null) existente.setCantidadPersonas(dto.cantidadPersonas);
            if(dto.nota != null) existente.setNota(dto.nota);
            if (dto.estado != null) existente.setEstado(com.uisil.restaurante.restaurante_pro_backend.model.EstadoReserva.valueOf(dto.estado));

            Reserva actualizada = reservaService.actualizarReservacion(reservaId, existente);
            return ResponseEntity.ok(actualizada);
        } catch (RecursoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la reservacion: " + e.getMessage());
        }
    }

    @DeleteMapping("/{reservaId}")
    public ResponseEntity<?> eliminarReservacion(@PathVariable("reservaId") Long reservaId) {
        try {
            Reserva r = reservaService.obtenerReservacionPorId(reservaId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", reservaId));

            // Si el estado es null, evita NPE; sólo prohibimos si es explícitamente CONFIRMADO
            if (r.getEstado() != null && r.getEstado() == com.uisil.restaurante.restaurante_pro_backend.model.EstadoReserva.CONFIRMADO) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No se puede eliminar una reserva confirmada.");
            }

            reservaService.eliminarReservacion(reservaId);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la reservación: " + ex.getMessage());
        }
    }

}
