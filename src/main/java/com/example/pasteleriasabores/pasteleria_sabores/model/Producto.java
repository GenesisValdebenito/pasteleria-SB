package com.example.pasteleriasabores.pasteleria_sabores.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String nombre;
    private String categoria;     // categoriaId del frontend se enviará aquí
    private String imagen;        // img del frontend

    @Column(columnDefinition = "TEXT")
    private String descripcion;   // desc del frontend
    private Integer stock;        // stock real
    private Double precio;
    private Boolean destacado;
    private Boolean oferta;
    private String tipo;          // circular, cuadrada, unit, etc
    private String tamano;        // mediana, grande, unit
}
