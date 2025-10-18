package com.uisil.restaurante.restaurante_pro_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mesa")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "reservas"})
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mesa_id")
    private Long mesaId;

    @Column(name = "numero_mesa")
    private Integer numeroMesa;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name = "estado")
    private String estado;

}
