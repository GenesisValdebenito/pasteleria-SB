package com.example.pasteleriasabores.pasteleria_sabores.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false, length = 255)
    private String password;
    
    @Column(length = 20)
    private String telefono;
    
    @Column(columnDefinition = "TEXT")
    private String direccion;
    
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;
    
    @Column(nullable = false, length = 20)
    private String rol = "cliente"; // cliente, admin, moderador
    
    @Column(nullable = false, length = 20)
    private String estado = "activo"; // activo, inactivo, bloqueado
    
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
    
    @Column(name = "ultimo_acceso")
    private LocalDateTime ultimoAcceso;
}
