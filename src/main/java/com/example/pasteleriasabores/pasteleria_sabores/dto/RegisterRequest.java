package com.example.pasteleriasabores.pasteleria_sabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String nombre;
    private String email;
    private String password;
    private String confirmPassword;
    private String telefono;
    private String direccion;
    private String fechaNacimiento;
}
