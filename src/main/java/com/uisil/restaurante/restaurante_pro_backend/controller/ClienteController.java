package com.uisil.restaurante.restaurante_pro_backend.controller;

import com.uisil.restaurante.restaurante_pro_backend.model.Cliente;
import com.uisil.restaurante.restaurante_pro_backend.model.Reserva;
import com.uisil.restaurante.restaurante_pro_backend.service.IClienteService;
import com.uisil.restaurante.restaurante_pro_backend.service.IReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Operaciones de gestion de clientes")
public class ClienteController {

    private final IClienteService clienteService;
    private final IReservaService reservaService;

    // Crear un cliente
    @PostMapping
    @Operation(summary = "Crear cliente")
    @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Cliente creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente){
        Cliente nuevoCliente = clienteService.crearCliente(cliente);

                return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    // Obtener todos los clientes
    @GetMapping
    @Operation(summary = "Listar clientes")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    public ResponseEntity<List<Cliente>> obtenerTodos(){
        List<Cliente> clientes = clienteService.obtenerTodosClientes();

        return  ResponseEntity.ok(clientes);
    }

    // Obtener Cliente por clienteId
    @GetMapping("/{clienteId}")
    @Operation(summary = "Obtener cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long clienteId){
        Optional<Cliente>  cliente = clienteService.obtenerClientePorId(clienteId);

        return cliente
                .map(ResponseEntity::ok) /// Si esta presente devuelve 200 ok
                .orElseGet(() -> ResponseEntity.notFound().build()); // si esta vacio devuelve 404 not found
    }

    @PutMapping("/{clienteId}")
    @Operation(summary = "Actualizar cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long clienteId, @RequestBody Cliente actualizarCliete){
        Cliente clienteEditado = clienteService.actualizarCliente(clienteId, actualizarCliete);
        return  ResponseEntity.ok(clienteEditado);
    }

    // eliminar
    @DeleteMapping("/{clienteId}")
    @Operation(summary = "Eliminar cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente eliminado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long clienteId){
        clienteService.eliminarCliente(clienteId);

        return ResponseEntity.noContent().build();
    }

    // NEW: /api/clientes/activos?days=30  -> cuenta clientes únicos con reservas en el periodo
    @GetMapping("/activos")
    @Operation(
            summary = "Contar clientes activos",
            description = "Devuelve la cantidad de clientes unicos con reservas desde los ultimos N dias."
    )
    @ApiResponse(responseCode = "200", description = "Conteo generado")
    public ResponseEntity<Map<String,Integer>> clientesActivos(@RequestParam(value="days", defaultValue="30") int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Reserva> recientes = reservaService.obtenerTodasLasReservaciones()
                .stream()
                .filter(r -> r.getFechaHoraInicio() != null && r.getFechaHoraInicio().isAfter(since))
                .collect(Collectors.toList());
        Set<Long> uniqueClients = recientes.stream()
                .filter(r -> r.getCliente() != null)
                .map(r -> r.getCliente().getClienteId())
                .collect(Collectors.toSet());
        return ResponseEntity.ok(Map.of("active", uniqueClients.size()));
    }
}
