package com.example.pasteleriasabores.pasteleria_sabores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pasteleriasabores.pasteleria_sabores.dto.PasswordChangeRequest;
import com.example.pasteleriasabores.pasteleria_sabores.model.Usuario;
import com.example.pasteleriasabores.pasteleria_sabores.service.UsuarioService;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    // Obtener perfil del usuario actual
    @GetMapping("/perfil")
    public ResponseEntity<?> getPerfil(@RequestHeader("Authorization") String token) {
        // En una app real, extraerías el ID del usuario del token JWT
        // Por ahora, simulamos que el usuario con ID 1 es el actual
        Long userId = 1L;
        
        return usuarioService.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Actualizar perfil
    @PutMapping("/perfil")
    public ResponseEntity<?> updatePerfil(@RequestHeader("Authorization") String token,
                                         @RequestBody Usuario usuarioDetails) {
        try {
            Long userId = 1L; // Simulado
            var updatedUser = usuarioService.updateProfile(userId, usuarioDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Cambiar contraseña
    @PutMapping("/cambiar-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token,
                                           @RequestBody PasswordChangeRequest passwordRequest) {
        try {
            Long userId = 1L; // Simulado
            
            if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Las contraseñas no coinciden");
            }
            
            usuarioService.changePassword(userId, passwordRequest.getCurrentPassword(), passwordRequest.getNewPassword());
            return ResponseEntity.ok("Contraseña actualizada correctamente");
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}