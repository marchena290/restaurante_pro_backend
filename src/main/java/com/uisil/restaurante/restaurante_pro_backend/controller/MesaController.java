package com.uisil.restaurante.restaurante_pro_backend.controller;


import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;
import com.uisil.restaurante.restaurante_pro_backend.service.IMesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
@Tag(name = "Mesas", description = "Operaciones de gestion de mesas")
public class MesaController {

    private final IMesaService mesaService;

    @PostMapping
    @Operation(summary = "Crear mesa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Mesa creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<Mesa> crearMesa(@RequestBody @Valid Mesa crearMesa){
        Mesa nuevaMesa = mesaService.crearMesa(crearMesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMesa);
    }

    @GetMapping
    @Operation(summary = "Listar mesas")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<Mesa>> obtenerTodos(){
        List<Mesa> mesas = mesaService.obtenerTodasLasMesas();
        return ResponseEntity.ok(mesas);
    }

    // FIX: usar {mesaId} y PathVariable con el mismo nombre
    @GetMapping("/{mesaId}")
    @Operation(summary = "Obtener mesa por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mesa encontrada"),
            @ApiResponse(responseCode = "404", description = "Mesa no encontrada")
    })
    public ResponseEntity<Mesa> obtener(@PathVariable("mesaId") Long mesaId){
        Mesa mesa = mesaService.obtenerMesaPorId(mesaId);
        return ResponseEntity.ok(mesa);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar mesa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mesa actualizada"),
            @ApiResponse(responseCode = "404", description = "Mesa no encontrada")
    })
    public ResponseEntity<Mesa> actualizar(@PathVariable(name = "id") Long mesaId, @RequestBody @Valid Mesa actualizarMesa){
        Mesa mesaActualizada = mesaService.actualizarMesa(mesaId, actualizarMesa);

        return ResponseEntity.ok(mesaActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar mesa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Mesa eliminada"),
            @ApiResponse(responseCode = "404", description = "Mesa no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable(name = "id") Long mesaId){
        mesaService.eliminarMesa(mesaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ocupadas")
    @Operation(
            summary = "Obtener ocupacion de mesas",
            description = "Devuelve conteo de mesas ocupadas y total para indicadores del dashboard."
    )
    @ApiResponse(responseCode = "200", description = "Indicadores generados")
    public ResponseEntity<Map<String,Integer>> obtenerMesasOcupadas() {
        List<Mesa> mesas = mesaService.obtenerTodasLasMesas();
        int total = mesas.size();
        int occupied = (int) mesas.stream()
                .filter(m -> m.getEstado() != null && !m.getEstado().name().equalsIgnoreCase("DISPONIBLE"))
                .count();
        return ResponseEntity.ok(Map.of("occupied", occupied, "total", total));
    }
}
