package com.example.pasteleriasabores.pasteleria_sabores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.pasteleriasabores.pasteleria_sabores.dto.PasswordChangeRequest;
import com.example.pasteleriasabores.pasteleria_sabores.dto.UsuarioResponse;
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
    public ResponseEntity<?> getPerfil() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return usuarioService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
    // Actualizar perfil
   @PutMapping("/perfil")
    public ResponseEntity<?> updatePerfil(@RequestBody Usuario datos) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        try {
            UsuarioResponse updated = usuarioService.updateProfileByEmail(email, datos);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
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