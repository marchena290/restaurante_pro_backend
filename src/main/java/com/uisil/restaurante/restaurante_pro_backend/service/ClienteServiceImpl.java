package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.exception.EmailDuplicadoException;
import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.Cliente;
import com.uisil.restaurante.restaurante_pro_backend.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements IClienteService {

    // Inyección
    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente crearCliente(Cliente cliente) {
        Optional<Cliente> existeCliente = clienteRepository.findByEmail(cliente.getEmail());

        if (existeCliente.isPresent()){
            throw new EmailDuplicadoException("El email introducido ya existe");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public List<Cliente> obtenerTodosClientes(){
        return clienteRepository.findAll();
    }

    @Override
    public Optional<Cliente> obtenerClientePorId(Long id){
        Cliente clienteEncontrado = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));

        return Optional.of(clienteEncontrado);
    }

    @Override
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Cliente" , id);
        }
        clienteRepository.deleteById(id);
    }
}
