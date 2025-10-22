package com.uisil.restaurante.restaurante_pro_backend.model.security;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_rol", length = 20)
    private NombreRol nombreRol;
}
