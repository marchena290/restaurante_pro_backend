package com.uisil.restaurante.restaurante_pro_backend.controller;

import com.uisil.restaurante.restaurante_pro_backend.exception.PeticionInvalida;
import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.Cliente;
import com.uisil.restaurante.restaurante_pro_backend.model.EstadoReserva;
import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;
import com.uisil.restaurante.restaurante_pro_backend.model.Reserva;
import com.uisil.restaurante.restaurante_pro_backend.repository.ClienteRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.MesaRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.ReservaRepository;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.ChangeEstadoDto;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.CreateReservaDto;
import com.uisil.restaurante.restaurante_pro_backend.security.dto.UpdateReservaDto;
import com.uisil.restaurante.restaurante_pro_backend.service.IReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final IReservaService reservaService;
    private final ClienteRepository clienteRepository;
    private final MesaRepository mesaRepository;
    private final ReservaRepository reservaRepository;

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

    // Nuevo: stats para dashboard -> /api/reservas/stats?days=7
    @GetMapping("/stats")
    public ResponseEntity<List<Map<String,Object>>> statsReservas(@RequestParam(value = "days", defaultValue = "7") int days) {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(Math.max(0, days - 1));
        List<Reserva> reservas = reservaService.obtenerTodasLasReservaciones();

        Map<String, Long> countsByDate = reservas.stream()
                .filter(r -> r.getFechaHoraInicio() != null)
                .filter(r -> {
                    LocalDate fecha = r.getFechaHoraInicio().toLocalDate();
                    return !fecha.isBefore(start) && !fecha.isAfter(today);
                })
                .collect(Collectors.groupingBy(r -> r.getFechaHoraInicio().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        Collectors.counting()));

        List<Map<String,Object>> result = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            LocalDate d = start.plusDays(i);
            String key = d.format(DateTimeFormatter.ISO_LOCAL_DATE);
            Long cnt = countsByDate.getOrDefault(key, 0L);
            Map<String,Object> entry = new HashMap<>();
            entry.put("date", key);
            entry.put("count", cnt.intValue());
            result.add(entry);
        }
        return ResponseEntity.ok(result);
    }

    // Obtener por id (asegurarse que el nombre coincide con PathVariable)
    @GetMapping("/{reservaId}")
    public ResponseEntity<Reserva> obtenerReservacionPorId(@PathVariable("reservaId") Long reservaId){
        return reservaService.obtenerReservacionPorId(reservaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{reservaId}")
    public  ResponseEntity<?> actualizarReservacion(@PathVariable("reservaId") Long reservaId, @RequestBody @Valid UpdateReservaDto dto){
        try {
            Reserva existente = reservaService.obtenerReservacionPorId(reservaId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Reserva", reservaId));

            // NO permitir cambiar el cliente de la reserva existente.
            if (dto.clienteId != null && !dto.clienteId.equals(existente.getCliente().getClienteId())) {
                return ResponseEntity.badRequest().body("No está permitido cambiar el cliente de una reserva existente.");
            }

            // Si viene mesaId, resolver y asignar
            if(dto.mesaId != null) {
                Mesa mesa = mesaRepository.findById(dto.mesaId)
                        .orElseThrow(()-> new RecursoNoEncontradoException("Mesa", dto.mesaId));
                existente.setMesaId(mesa);
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

    // dentro de ReservaController
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<?> changeEstado(@PathVariable Long id, @RequestBody ChangeEstadoDto dto) {
        // Buscar reserva
        Optional<Reserva> optional = reservaRepository.findById(id);
        if (!optional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "Reserva no encontrada"));
        }

        Reserva reserva = optional.get();

        // Validar payload
        if (dto == null || dto.estado == null || dto.estado.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "Estado requerido"));
        }

        // Convertir string a enum EstadoReserva (manejar estado inválido)
        EstadoReserva nuevoEstado;
        try {
            nuevoEstado = EstadoReserva.valueOf(dto.estado.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "Estado inválido"));
        }

        // Reglas de negocio (ejemplo: no reabrir FINALIZADA)
        if (reserva.getEstado() == EstadoReserva.FINALIZADA && nuevoEstado == EstadoReserva.EN_CURSO) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("mensaje", "No se puede reabrir una reserva finalizada"));
        }

        // Aplicar cambio
        reserva.setEstado(nuevoEstado);
        if (dto.fechaConfirmacion != null) {
            reserva.setFechaConfirmacion(dto.fechaConfirmacion);
        }

        // Guardar y devolver
        Reserva saved = reservaRepository.save(reserva);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/{id}/checkin")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLEADO')")
    public ResponseEntity<?> checkIn(@PathVariable("id") Long id, Principal principal) {
        try {
            String username = principal != null ? principal.getName() : null;
            Reserva updated = reservaService.checkInReserva(id, username);
            return ResponseEntity.ok(updated);
        } catch (RecursoNoEncontradoException ex) {
            return ResponseEntity.status(404).body(Map.of("mensaje", ex.getMessage()));
        } catch (PeticionInvalida ex) {
            return ResponseEntity.status(400).body(Map.of("mensaje", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al procesar check-in", "error", ex.getMessage()));
        }
    }
}
