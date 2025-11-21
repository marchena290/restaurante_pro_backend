package com.uisil.restaurante.restaurante_pro_backend.service;

import com.uisil.restaurante.restaurante_pro_backend.exception.PeticionInvalida;
import com.uisil.restaurante.restaurante_pro_backend.exception.RecursoNoEncontradoException;
import com.uisil.restaurante.restaurante_pro_backend.model.EstadoReserva;
import com.uisil.restaurante.restaurante_pro_backend.model.Mesa;
import com.uisil.restaurante.restaurante_pro_backend.model.MesaEstado;
import com.uisil.restaurante.restaurante_pro_backend.repository.MesaRepository;
import com.uisil.restaurante.restaurante_pro_backend.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MesaServiceImpl implements IMesaService{

    // Inyección de los repositorios
    private final MesaRepository mesaRepository;
    private final ReservaRepository reservaRepository;

    // ----------------------------------------------------
    //  MÉTODOS PRIVADOS DE VALIDACIÓN
    // ----------------------------------------------------

    // Válida que el número de mesa no esté duplicado
    public void validarNumeroMesaUnico(Integer numeroMesa, Long idExcluir){
        // Busca si ya existe una mesa con ese número
        Optional<Mesa> mesaExistente = mesaRepository.findByNumeroMesa(numeroMesa);

        // !Objects.equals comparar valor a un sean nulos
        if (mesaExistente.isPresent() && !Objects.equals(mesaExistente.get().getMesaId(), idExcluir)){
            throw new PeticionInvalida("Ya existe una mesa con el número: " + numeroMesa);
        }
        // Si la encuentra Y el ID no es el mismo que estamos editando, lanza error 400.
    }

    // Válida que la capacidad sea un número positivo (> 0)
    public void validarCapacidad(Integer capacidad){
        if (capacidad == null || capacidad <= 0) {
            throw new PeticionInvalida("La capacidad de la mesa debe ser un valor entero mayor a cero");
        }
    }

    // Válida que la mesa NO tenga reservas activas antes de eliminarla (Integridad referencial)
    public void validarReservasActivas(Long mesaId){
        // Buscamos la mesa (si no existe, lanza 404 aquí mismo)
        Mesa mesa = obtenerMesaPorId(mesaId);

        long reservasActivas = reservaRepository.countByMesaIdAndEstadoNot(mesa, EstadoReserva.FINALIZADA);

        // Contamos cuántas reservas para esta mesa NO están FINALIZADAS.
        // Si el resultado es > 0, significa que la mesa está en uso.
        if (reservasActivas > 0){
            throw new PeticionInvalida("No se puede eliminar la mesa. Tiene " + reservasActivas + " reservas activas o futuras asociadas");
        }
    }

    // ----------------------------------------------------
    //  IMPLEMENTACIÓN DEL CRUD
    // ----------------------------------------------------

    @Override
    public Mesa crearMesa(Mesa mesa){
        // Aplicamos las validaciones antes de guardar
        validarCapacidad(mesa.getCapacidad());
        validarNumeroMesaUnico(mesa.getNumeroMesa(), null);

        mesa.setEstado(parseEstado(mesa.getEstado()));

        return mesaRepository.save(mesa);
    }

    @Override
    public List<Mesa> obtenerTodasLasMesas(){
        return mesaRepository.findAll();
    }

    @Override
    public Mesa obtenerMesaPorId(Long mesaId){
        return mesaRepository.findById(mesaId)
                .orElseThrow(()-> new RecursoNoEncontradoException("Mesa " , mesaId));
    }

    @Override
    public Mesa actualizarMesa(Long mesaId, Mesa actualizarMesa){
        // Busca la mesa original y si existe, aplica los cambios
        return mesaRepository.findById(mesaId)
                .map(mesaOriginal ->{

                    // Validaciones (excluyendo el ID actual, ya que es un PUT)
                    validarCapacidad(actualizarMesa.getCapacidad());
                    validarNumeroMesaUnico(actualizarMesa.getNumeroMesa(), mesaId);

                    // Actualización de los campos
                    mesaOriginal.setNumeroMesa(actualizarMesa.getNumeroMesa());
                    mesaOriginal.setCapacidad(actualizarMesa.getCapacidad());
                    mesaOriginal.setEstado(parseEstado(actualizarMesa.getEstado()));

                    return mesaRepository.save(mesaOriginal);
                })
                .orElseThrow(()-> new RecursoNoEncontradoException("Mesa ", mesaId));
    }

    @Override
    public void eliminarMesa(Long mesaId){
        // 1. Validamos que no tenga reservas activas
        validarReservasActivas(mesaId);

        // 2. Si pasa la validación, procedemos a eliminar.
        Mesa mesa = obtenerMesaPorId(mesaId);
        mesaRepository.delete(mesa);
    }

    // Normaliza distintos formatos de estado (enum, texto o código numérico)
    private MesaEstado parseEstado(Object e) {
        if (e == null) return MesaEstado.DISPONIBLE;

        // Si ya es el enum, devolverlo directamente
        if (e instanceof MesaEstado) {
            return (MesaEstado) e;
        }

        // Si es número, mapear por código
        if (e instanceof Number) {
            int n = ((Number) e).intValue();
            switch (n) {
                case 2: return MesaEstado.OCUPADA;
                case 3: return MesaEstado.RESERVADA;
                case 4: return MesaEstado.MANTENIMIENTO;
                default: return MesaEstado.DISPONIBLE;
            }
        }

        // Tratar cadenas (ej: "1", "DISPONIBLE", "Disponible")
        String s = e.toString().trim();
        if (s.isEmpty()) return MesaEstado.DISPONIBLE;

        // intentar como texto de enum
        try {
            return MesaEstado.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException ignored) {}

        // intentar como código numérico en String
        switch (s) {
            case "1": return MesaEstado.DISPONIBLE;
            case "2": return MesaEstado.OCUPADA;
            case "3": return MesaEstado.RESERVADA;
            case "4": return MesaEstado.MANTENIMIENTO;
            default: return MesaEstado.DISPONIBLE;
        }
    }
}
