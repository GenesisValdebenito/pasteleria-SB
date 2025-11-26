package com.example.pasteleriasabores.pasteleria_sabores.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sku;   // SKU generado aleatoriamente
    private String imagen;
    private String nombre;
    private String categoria;
    private String descripcion;
    private Integer cantidad;
    private Double precio;

    public Producto(String imagen, String nombre, String categoria, String descripcion, Integer cantidad, Double precio) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
        this.sku = ""; // El SKU se establecerá al crear el producto
    }
}
