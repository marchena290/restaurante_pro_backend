package com.uisil.restaurante.restaurante_pro_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reserva")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reserva {

    // 1. CLAVE PRIMARIA (PK)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserva_id")
    private Long reservaId;

    // FK: CLIENTE (¡RELACIÓN PRINCIPAL!)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesaId;

    // CAMPOS DE TIEMPO Y CANTIDAD
    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(name = "cantidad_personas", nullable = false)
    private Integer cantidadPersonas;

    // CAMPOS DE ESTADO Y NOTAS
    @Column(name = "estado", length = 20)
    private String estado;

    @Column(name = "nota")
    private String nota;

    // CAMPOS DE AUDITORÍA (Pueden ser nulos)
    @Column(name = "fecha_confirmacion", nullable = true)
    private LocalDateTime fechaConfirmacion;

    @Column(name = "fecha_Fin_Real", nullable = true)
    private LocalDateTime fechaFinReal;

}
