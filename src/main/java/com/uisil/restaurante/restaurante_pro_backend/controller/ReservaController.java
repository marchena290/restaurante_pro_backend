package com.uisil.restaurante.restaurante_pro_backend.controller;

import com.uisil.restaurante.restaurante_pro_backend.model.Reserva;
import com.uisil.restaurante.restaurante_pro_backend.service.IReservaService;
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

    // Crear una reservacion
    @PostMapping
    public ResponseEntity<Reserva> crearReservacion(@RequestBody Reserva crearReserva){
        Reserva nuevaReserva = reservaService.crearReservacion(crearReserva);

        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    // Mostrar todas las reservaciones
    @GetMapping
    public ResponseEntity<List<Reserva>> obtenerTodasLasReservaciones() {
        List<Reserva> reservas = reservaService.obtenerTodasLasReservaciones();

        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtenerReservacionPorId(@PathVariable Long reservacionId){
        Reserva reserva = reservaService.obtenerReservacionPorId(reservacionId)
                .orElse(null); //Usamos orElse(null) para un manejo limpio del Optional

        if (reserva !=null) {
            return ResponseEntity.ok(reserva); // Código 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Código 404 NOT FOUND
    } }

    @PutMapping("/{id}")
    public  ResponseEntity<Reserva> actualizarReservacion(@PathVariable(name = "id") Long reservacionId, @RequestBody Reserva actualizarReserva){
        Reserva reservaActualizada = reservaService.actualizarReservacion(reservacionId, actualizarReserva);

        return ResponseEntity.ok(reservaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReservacion(@PathVariable(name = "id") Long reservacionId){
        reservaService.eliminarReservacion(reservacionId);

        return ResponseEntity.noContent().build();
    }

}
