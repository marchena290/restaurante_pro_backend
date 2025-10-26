package com.uisil.restaurante.restaurante_pro_backend.model.security;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "rol")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rol implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_rol", length = 20, unique = true, nullable = false)
    private NombreRol nombreRol;

    @Override
    public  String getAuthority(){
        return nombreRol.name();
    }
}
