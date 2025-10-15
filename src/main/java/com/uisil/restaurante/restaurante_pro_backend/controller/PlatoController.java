package com.uisil.restaurante.restaurante_pro_backend.controller;

import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.Plato;
import com.uisil.restaurante.restaurante_pro_backend.service.IPlatoService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final IPlatoService platoService;

    // Crear un plato
    @PostMapping
    public ResponseEntity<Plato> crearPlato(@RequestBody Plato crearPlato)  {
        Plato nuevoPlato = platoService.crearPlato(crearPlato);
        return ResponseEntity.ok(nuevoPlato);
    }

    //Obtener todos los platos
    @GetMapping
    public ResponseEntity<List<Plato>> obtenerTodos(){
        List<Plato> platos = platoService.obtenerTodosLosPlatos();
        return ResponseEntity.ok(platos);
    }

    // Obtener plato por platoId
    @GetMapping("/{platoId}")
    public ResponseEntity<Plato> obtenerPorId(@PathVariable Long platoId){
        Optional<Plato> plato = platoService.obtenerPlatoPorId(platoId);

        return plato
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plato ", platoId));
    }

    // Actualizar plato
    @PutMapping("/{id}")
    public ResponseEntity<Plato> actualizarPlato(@PathVariable Long platoId, @RequestBody Plato platoActualizado){
        Plato actualizarPlato = platoService.actualizarPlato(platoId, platoActualizado);
        return ResponseEntity.ok(actualizarPlato);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlato(@PathVariable Long platoId){
        platoService.eliminarPlato(platoId);
        return ResponseEntity.noContent().build();
    }

}
