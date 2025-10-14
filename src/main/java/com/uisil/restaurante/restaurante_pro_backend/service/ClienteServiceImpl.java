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
    public Cliente crearCliente(Cliente crearcliente) {
        Optional<Cliente> existeCliente = clienteRepository.findByEmail(crearcliente.getEmail());

        if (existeCliente.isPresent()){
            throw new EmailDuplicadoException("El email introducido ya existe");
        }
        return clienteRepository.save(crearcliente);
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
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado){
        return clienteRepository.findById(id)
                .map(clienteExistente -> {

                    String nuevoEmail = clienteActualizado.getEmail();
                    // Si el nuevo email es diferente al que ya tiene el cliente existente,
                    // Validamos unicidad
                    if (!clienteExistente.getEmail().equals(nuevoEmail)){

                        // Verificar si el nuevo email ya existe en OTRO cliente.
                        if (clienteRepository.findByEmail(nuevoEmail).isPresent()){
                            throw new EmailDuplicadoException("El email no esta disponible, porque esta siendo usando por otro cliente.");
                        }

                        clienteActualizado.setEmail(nuevoEmail);
                    }
                    clienteExistente.setNombre(clienteActualizado.getNombre());
                    clienteExistente.setTelefono(clienteActualizado.getTelefono());
                    clienteExistente.setDireccion(clienteActualizado.getDireccion());

                    return clienteRepository.save(clienteExistente);
        })
         .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));
    }

    @Override
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Cliente" , id);
        }
        clienteRepository.deleteById(id);
    }
}
