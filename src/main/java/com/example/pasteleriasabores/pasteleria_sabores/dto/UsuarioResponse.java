package com.example.pasteleriasabores.pasteleria_sabores.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private String fechaNacimiento;
    
    private String rol;
    private String estado;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;
   
}