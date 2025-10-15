package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.model.Cliente;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IClienteService {
    // Metodos CRUD

    // Crear y guardar Cliente
    Cliente crearCliente(Cliente crearcliente);

    // Obtener todos los Clientes
    List<Cliente> obtenerTodosClientes();

    // Obtener Cliente por clienteId
    Optional<Cliente> obtenerClientePorId(Long clienteId);

    // Actualizar Cliente
   Cliente actualizarCliente(Long clienteId, Cliente clienteActualizado);

    // Eliminar cliente
    void eliminarCliente(Long clienteId);
}
