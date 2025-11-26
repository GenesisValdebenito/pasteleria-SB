package com.example.pasteleriasabores.pasteleria_sabores.controller;

import com.example.pasteleriasabores.pasteleria_sabores.model.Rol;
import com.example.pasteleriasabores.pasteleria_sabores.model.Usuario;
import com.example.pasteleriasabores.pasteleria_sabores.repository.RolRepository;
import com.example.pasteleriasabores.pasteleria_sabores.repository.UsuarioRepository;
import com.example.pasteleriasabores.pasteleria_sabores.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/register")
    public String registrar(@RequestBody Usuario usuario) {

        usuario.setPassword(encoder.encode(usuario.getPassword()));

        Rol rolCliente = rolRepository.findByNombre("USER");
        usuario.getRoles().add(rolCliente);

        usuarioRepository.save(usuario);

        return "Usuario registrado";
    }

    @PostMapping("/login")
    public String login(@RequestBody Usuario usuario) {

        Usuario userDB = usuarioRepository.findByEmail(usuario.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        if (!encoder.matches(usuario.getPassword(), userDB.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        Rol rol = userDB.getRoles().stream().findFirst().orElse(null);

        return jwtUtil.generarToken(userDB.getEmail(), rol.getNombre());
    }
}
