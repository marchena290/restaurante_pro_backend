package com.uisil.restaurante.restaurante_pro_backend.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "clientes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "telefono",length = 20)
    private Number telefono;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

}
