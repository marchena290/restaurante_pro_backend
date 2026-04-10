package com.uisil.restaurante.restaurante_pro_backend.controller;

import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.Plato;
import com.uisil.restaurante.restaurante_pro_backend.service.IPlatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/platos")
@RequiredArgsConstructor
@Tag(name = "Platos", description = "Operaciones de gestion de platos")
public class PlatoController {

    private final IPlatoService platoService;

    // Crear un plato
    @PostMapping
    @Operation(summary = "Crear plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<Plato> crearPlato(@RequestBody Plato crearPlato)  {
        Plato nuevoPlato = platoService.crearPlato(crearPlato);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPlato);
    }

    //Obtener todos los platos
    @GetMapping
    @Operation(summary = "Listar platos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<Plato>> obtenerTodos(){
        List<Plato> platos = platoService.obtenerTodosLosPlatos();
        return ResponseEntity.ok(platos);
    }

    // Obtener plato por platoId
    @GetMapping("/{platoId}")
    @Operation(summary = "Obtener plato por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato encontrado"),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado")
    })
    public ResponseEntity<Plato> obtenerPorId(@PathVariable Long platoId){
        Optional<Plato> plato = platoService.obtenerPlatoPorId(platoId);

        return plato
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plato ", platoId));
    }

    // Actualizar plato
    @PutMapping("/{platoId}")
    @Operation(summary = "Actualizar plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado"),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado")
    })
    public ResponseEntity<Plato> actualizarPlato(@PathVariable Long platoId, @RequestBody Plato platoActualizado){
        Plato actualizarPlato = platoService.actualizarPlato(platoId, platoActualizado);
        return ResponseEntity.ok(actualizarPlato);

    }

    @DeleteMapping("/{platoId}")
    @Operation(summary = "Eliminar plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plato eliminado"),
            @ApiResponse(responseCode = "404", description = "Plato no encontrado")
    })
    public ResponseEntity<Void> eliminarPlato(@PathVariable Long platoId){
        platoService.eliminarPlato(platoId);
        return ResponseEntity.noContent().build();
    }

}
