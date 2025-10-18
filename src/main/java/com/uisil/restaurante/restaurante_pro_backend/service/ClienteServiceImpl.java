package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.exception.EmailDuplicadoException;
import com.uisil.restaurante.restaurante_pro_backend.exception.PeticionInvalida;
import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.Cliente;
import com.uisil.restaurante.restaurante_pro_backend.model.EstadoReserva;
import com.uisil.restaurante.restaurante_pro_backend.repository.ClienteRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements IClienteService {

    // Inyección
    private final ClienteRepository clienteRepository;
    private final ReservaRepository reservaRepository;

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
    public Optional<Cliente> obtenerClientePorId(Long clienteId){
        Cliente clienteEncontrado = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", clienteId));

        return Optional.of(clienteEncontrado);
    }

    @Override
    public Cliente actualizarCliente(Long clienteId, Cliente clienteActualizado){
        return clienteRepository.findById(clienteId)
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
         .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", clienteId));
    }

    @Override
    public void eliminarCliente(Long clienteId) {

        // ver si el cliente existe
        Cliente clienteEncontrado = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente ", clienteId));

        long reservasActivas  = reservaRepository.countByCliente_ClienteIdAndEstadoNot(clienteId, EstadoReserva.FINALIZADA);

        // Ver si el cliente tiene un reserva Activa
        if (reservasActivas > 0){
            throw new PeticionInvalida("No se puede eliminar el cliente porque tiene " + reservasActivas + " reservas activas o futuras asociadas.");
        }

        // Eliminar Cliente
        clienteRepository.delete(clienteEncontrado);
    }
}
