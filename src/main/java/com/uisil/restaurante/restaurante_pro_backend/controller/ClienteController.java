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

    // Obtener Cliente por id
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id){
        Optional<Cliente>  cliente = clienteService.obtenerClientePorId(id);

        return cliente
                .map(ResponseEntity::ok) /// Si esta presente devuelve 200 ok
                .orElseGet(() -> ResponseEntity.notFound().build()); // si esta vacio devuelve 404 not found
    }

    // eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id){
        clienteService.eliminarCliente(id);

        return ResponseEntity.notFound().build();
    }

}
