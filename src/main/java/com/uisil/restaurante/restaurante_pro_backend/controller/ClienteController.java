package com.uisil.restaurante.restaurante_pro_backend.controller;

import com.uisil.restaurante.restaurante_pro_backend.model.Cliente;
import com.uisil.restaurante.restaurante_pro_backend.service.IClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final IClienteService clienteService;

    // Crear un cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente){
        Cliente nuevoCliente = clienteService.crearCliente(cliente);

        return  ResponseEntity.ok(nuevoCliente);
    }

    // Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodos(){
        List<Cliente> clientes = clienteService.obtenerTodosClientes();

        return  ResponseEntity.ok(clientes);
    }

    // Obtener Cliente por clienteId
    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long clienteId){
        Optional<Cliente>  cliente = clienteService.obtenerClientePorId(clienteId);

        return cliente
                .map(ResponseEntity::ok) /// Si esta presente devuelve 200 ok
                .orElseGet(() -> ResponseEntity.notFound().build()); // si esta vacio devuelve 404 not found
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long clienteId, @RequestBody Cliente actualizarCliete){
        Cliente clienteEditado = clienteService.actualizarCliente(clienteId, actualizarCliete);
        return  ResponseEntity.ok(clienteEditado);
    }

    // eliminar
    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long clienteId){
        clienteService.eliminarCliente(clienteId);

        return ResponseEntity.noContent().build();
    }

}
