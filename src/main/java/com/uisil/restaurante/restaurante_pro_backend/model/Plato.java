package com.uisil.restaurante.restaurante_pro_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "plato")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plato_id")
    private Long platoId;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "disponible", nullable = false)
    private boolean disponible;

    @Column(name = "categoria_simple", length = 50)
    private String categoriaSimple;

}
