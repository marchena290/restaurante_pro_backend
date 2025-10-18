package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;

import java.util.List;

public interface IMesaService {

    //Metodos CRUD
    Mesa crearMesa(Mesa mesa);

    List<Mesa> obtenerTodasLasMesas();

    Mesa obtenerMesaPorId(Long mesaId);

    Mesa actualizarMesa(Long mesaId, Mesa actualizarMesa);

    void eliminarMesa(Long mesaId);
}
