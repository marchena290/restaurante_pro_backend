package com.uisil.restaurante.restaurante_pro_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
@Schema(name = "Mesa", description = "Entidad de mesa del restaurante")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mesa_id")
    @Schema(description = "Identificador de la mesa", example = "1")
    private Long mesaId;

    @Column(name = "numero_mesa")
    @Min(value = 1, message = "El numero de mesa debe ser mayor o igual a 1")
    @Max(value = 100, message = "El numero de mesa debe ser menor o igual a 100")
    @Schema(description = "Numero visible de la mesa", example = "10")
    private Integer numeroMesa;

    @Column(name = "capacidad")
    @Min(value = 1, message = "La capacidad debe ser mayor o igual a 1")
    @Max(value = 20, message = "La capacidad debe ser menor o igual a 20")
    @Schema(description = "Capacidad maxima de personas", example = "4")
    private Integer capacidad;

    @Column(name = "ubicacion")
    @Schema(description = "Ubicación de la mesa", example = "Terraza")
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    @Schema(description = "Estado actual de la mesa", example = "DISPONIBLE")
    private MesaEstado estado = MesaEstado.DISPONIBLE;
}
