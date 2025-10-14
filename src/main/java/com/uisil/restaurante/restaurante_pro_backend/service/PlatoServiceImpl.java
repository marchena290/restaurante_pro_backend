package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.exception.NombreDuplicadoException;
import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.Plato;
import com.uisil.restaurante.restaurante_pro_backend.repository.PlatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlatoServiceImpl implements IPlatoService{
    private final PlatoRepository platoRepository;

    @Override
    public Plato crearPlato(Plato crearPlato) {

        if(crearPlato.getPrecio().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("El precio del plato debe ser mayor a cero.");
        }

        // Nombre duplicado
        boolean platoExistente = platoRepository.existsByNombre(crearPlato.getNombre());
        if (platoExistente) {
            throw new NombreDuplicadoException("El nombre del plato " + crearPlato.getNombre() + "ya existe.");
        }

        return platoRepository.save(crearPlato);
    }

    @Override
    public List<Plato> obtenerTodosLosPlatos() {
        return platoRepository.findAll();
    }

    @Override
    public Optional<Plato> obtenerPlatoPorId(Long id) {
        Plato clienteEncontrado = platoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plato", id));

        return Optional.of(clienteEncontrado);
    }

    @Override
    public Plato actualizarPlato(Long id, Plato platoActualizado) {
        return platoRepository.findById(id)
                .map(platoExistente ->{
                    String platonuevo = platoActualizado.getNombre();

                    if (!platoExistente.getNombre().equalsIgnoreCase(platonuevo)){

                        if (platoRepository.existsByNombre(platoActualizado.getNombre())){
                            throw new NombreDuplicadoException("El nombre del plato " + platoActualizado.getNombre() + " ya está en uso");
                        }
                    }

                    platoExistente.setNombre(platoActualizado.getNombre());
                    platoExistente.setDescripcion(platoActualizado.getDescripcion());
                    platoExistente.setPrecio(platoActualizado.getPrecio());
                    platoExistente.setDisponible(platoActualizado.isDisponible());
                    platoExistente.setCategoriaSimple(platoActualizado.getCategoriaSimple());

                    return platoRepository.save(platoExistente);
                })
                .orElseThrow(() -> new RecursoNoEncontradoException("Plato", id));
    }

    @Override
    public void eliminarPlato(Long id) {
        if (!platoRepository.existsById(id)){
            throw new RecursoNoEncontradoException("Plato" , id);
        }
        platoRepository.deleteById(id);
    }
}
