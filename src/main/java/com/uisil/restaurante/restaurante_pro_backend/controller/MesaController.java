package com.uisil.restaurante.restaurante_pro_backend.controller;


import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;
import com.uisil.restaurante.restaurante_pro_backend.service.IMesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@RequiredArgsConstructor
public class MesaController {

    private final IMesaService mesaService;

    @PostMapping
    public ResponseEntity<Mesa> crearMesa(@RequestBody Mesa crearMesa){
        Mesa nuevaMesa = mesaService.crearMesa(crearMesa);
        return ResponseEntity.ok(nuevaMesa);
    }

    @GetMapping
    public ResponseEntity<List<Mesa>> obtenerTodos(){
        List<Mesa> mesas = mesaService.obtenerTodasLasMesas();
        return ResponseEntity.ok(mesas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesa> obtener(@PathVariable Long mesaId){
        Mesa mesa = mesaService.obtenerMesaPorId(mesaId);

        return ResponseEntity.ok(mesa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mesa> actualizar(@PathVariable(name = "id") Long mesaId, @RequestBody Mesa actualizarMesa){
        Mesa mesaActualizada = mesaService.actualizarMesa(mesaId, actualizarMesa);

        return ResponseEntity.ok(mesaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable(name = "id") Long mesaId){
        mesaService.eliminarMesa(mesaId);
        return ResponseEntity.noContent().build();
    }

}
