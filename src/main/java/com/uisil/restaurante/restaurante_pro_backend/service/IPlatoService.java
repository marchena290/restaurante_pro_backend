package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.model.Plato;

import java.util.List;
import java.util.Optional;

public interface IPlatoService {

    // Metodos CRUD
    // Crear un plato
    Plato crearPlato(Plato crearPlato) ;

    // Obtener Todo los platos
    List<Plato> obtenerTodosLosPlatos();

    // Obtener plato por platoId
    Optional<Plato> obtenerPlatoPorId(Long platoId);

    // Actualizar platillo
    Plato actualizarPlato(Long platoId, Plato actualizarPlato);

    // Eliminar Plato
    void eliminarPlato (Long platoId);
}
