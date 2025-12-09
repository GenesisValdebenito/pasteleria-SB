package com.example.pasteleriasabores.pasteleria_sabores.controller;

import com.example.pasteleriasabores.pasteleria_sabores.dto.LoginRequest;
import com.example.pasteleriasabores.pasteleria_sabores.dto.RegisterRequest;
import com.example.pasteleriasabores.pasteleria_sabores.dto.UsuarioResponse;
import com.example.pasteleriasabores.pasteleria_sabores.model.Usuario;
import com.example.pasteleriasabores.pasteleria_sabores.security.JwtUtil;
import com.example.pasteleriasabores.pasteleria_sabores.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil; // ✅ INYECTAR JWT

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ INYECTAR PASSWORD ENCODER

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<UsuarioResponse> usuarioOpt = usuarioService.authenticate(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            );
            
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(401).body("Credenciales inválidas");
            }
            
            UsuarioResponse user = usuarioOpt.get();
            
            // ✅ GENERAR JWT REAL
            String token = jwtUtil.generarToken(user.getEmail(), user.getRol());
            
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("token", token); // ✅ TOKEN JWT REAL
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error durante el login: " + e.getMessage());
        }
    }

   @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
    try {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Las contraseñas no coinciden");
        }

        if (registerRequest.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body("La contraseña debe tener al menos 6 caracteres");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registerRequest.getNombre());
        nuevoUsuario.setEmail(registerRequest.getEmail());
        nuevoUsuario.setTelefono(registerRequest.getTelefono());
        nuevoUsuario.setDireccion(registerRequest.getDireccion());
        nuevoUsuario.setFechaNacimiento(registerRequest.getFechaNacimiento());
        nuevoUsuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        nuevoUsuario.setEstado("activo");
        nuevoUsuario.setFechaRegistro(LocalDateTime.now());

        // ⭐ ASIGNAR ROL AUTOMÁTICO SEGÚN EMAIL
        String email = registerRequest.getEmail().toLowerCase();
        if (email.endsWith("@admin.com")) {
            nuevoUsuario.setRol("ADMIN");
        } 
        else if (email.endsWith("@test.com")) {
            nuevoUsuario.setRol("TEST");
        }
        else {
            nuevoUsuario.setRol("USER");
        }

        UsuarioResponse userResponse = usuarioService.register(nuevoUsuario);

        String token = jwtUtil.generarToken(userResponse.getEmail(), userResponse.getRol());

        Map<String, Object> response = new HashMap<>();
        response.put("user", userResponse);
        response.put("token", token);

        return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

}