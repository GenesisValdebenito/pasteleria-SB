package com.example.pasteleriasabores.pasteleria_sabores.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pasteleriasabores.pasteleria_sabores.dto.UsuarioResponse;
import com.example.pasteleriasabores.pasteleria_sabores.model.Usuario;
import com.example.pasteleriasabores.pasteleria_sabores.repository.UsuarioRepository;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
        
    @Autowired // ✅ INYECTAR PASSWORD ENCODER
    private PasswordEncoder passwordEncoder;


    // Convertir Usuario a UsuarioResponse (sin password)
    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setEmail(usuario.getEmail());
        response.setTelefono(usuario.getTelefono());
        response.setDireccion(usuario.getDireccion());
        response.setFechaNacimiento(usuario.getFechaNacimiento());
        response.setRol(usuario.getRol());
        response.setEstado(usuario.getEstado());
        response.setFechaRegistro(usuario.getFechaRegistro());
        response.setUltimoAcceso(usuario.getUltimoAcceso());
        return response;
    }

    // Autenticar usuario CORREGIDO
    public Optional<UsuarioResponse> authenticate(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()) && "activo".equals(user.getEstado())) // ✅ COMPARAR CON HASH
                .map(user -> {
                    // Actualizar último acceso
                    user.setUltimoAcceso(LocalDateTime.now());
                    usuarioRepository.save(user);
                    return toUsuarioResponse(user);
                });
    }
    
    // Registrar nuevo usuario - ELIMINAR DUPLICADO DE HASH (ya se hace en controller)
    public UsuarioResponse register(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        // Asegurar valores por defecto
        if (usuario.getRol() == null) {
            usuario.setRol("cliente");
        }
        if (usuario.getEstado() == null) {
            usuario.setEstado("activo");
        }
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDateTime.now());
        }
        
        // ✅ LA CONTRASEÑA YA VIENE HASHEDA DEL CONTROLLER
        Usuario savedUser = usuarioRepository.save(usuario);
        return toUsuarioResponse(savedUser);
    }
    
    // Obtener todos los usuarios
    public List<UsuarioResponse> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::toUsuarioResponse)
                .collect(Collectors.toList());
    }

    // Obtener usuario por ID
    public Optional<UsuarioResponse> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(this::toUsuarioResponse);
    }

    // Obtener usuario por email
    public Optional<UsuarioResponse> findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .map(this::toUsuarioResponse);
    }

    // Verificar si existe usuario por email
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // Actualizar usuario
    public UsuarioResponse update(Long id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Actualizar campos permitidos
        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setEmail(usuarioDetails.getEmail());
        usuario.setTelefono(usuarioDetails.getTelefono());
        usuario.setDireccion(usuarioDetails.getDireccion());
        usuario.setFechaNacimiento(usuarioDetails.getFechaNacimiento());

        // Solo admin puede cambiar rol y estado
        if (usuarioDetails.getRol() != null) {
            usuario.setRol(usuarioDetails.getRol());
        }
        if (usuarioDetails.getEstado() != null) {
            usuario.setEstado(usuarioDetails.getEstado());
        }

        Usuario updatedUser = usuarioRepository.save(usuario);
        return toUsuarioResponse(updatedUser);
    }

    // Actualizar perfil (sin cambiar rol/estado)
    public UsuarioResponse updateProfile(Long id, Usuario usuarioDetails) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setNombre(usuarioDetails.getNombre());
        usuario.setTelefono(usuarioDetails.getTelefono());
        usuario.setDireccion(usuarioDetails.getDireccion());
        usuario.setFechaNacimiento(usuarioDetails.getFechaNacimiento());

        // No permitir cambiar email desde el perfil
        // No permitir cambiar rol y estado desde el perfil

        Usuario updatedUser = usuarioRepository.save(usuario);
        return toUsuarioResponse(updatedUser);
    }

    
    // Cambiar contraseña CORREGIDO
    public void changePassword(Long id, String currentPassword, String newPassword) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        // ✅ COMPARAR CON HASH
        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }
        
        if (newPassword.length() < 6) {
            throw new RuntimeException("La nueva contraseña debe tener al menos 6 caracteres");
        }
        
        // ✅ HASH DE NUEVA CONTRASEÑA
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }

    // Cambiar estado de usuario
    public UsuarioResponse changeStatus(Long id, String estado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (!List.of("activo", "inactivo", "bloqueado").contains(estado)) {
            throw new RuntimeException("Estado no válido: " + estado);
        }

        usuario.setEstado(estado);
        Usuario updatedUser = usuarioRepository.save(usuario);
        return toUsuarioResponse(updatedUser);
    }

    // Cambiar rol de usuario
    public UsuarioResponse changeRole(Long id, String rol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        if (!List.of("cliente", "moderador", "admin").contains(rol)) {
            throw new RuntimeException("Rol no válido: " + rol);
        }

        usuario.setRol(rol);
        Usuario updatedUser = usuarioRepository.save(usuario);
        return toUsuarioResponse(updatedUser);
    }

    // Eliminar usuario
    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // Buscar usuarios por nombre
    public List<UsuarioResponse> searchByNombre(String nombre) {
        return usuarioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toUsuarioResponse)
                .collect(Collectors.toList());
    }

    // Obtener usuarios por rol
    public List<UsuarioResponse> findByRol(String rol) {
        return usuarioRepository.findByRol(rol).stream()
                .map(this::toUsuarioResponse)
                .collect(Collectors.toList());
    }

    // Obtener usuarios por estado
    public List<UsuarioResponse> findByEstado(String estado) {
        return usuarioRepository.findByEstado(estado).stream()
                .map(this::toUsuarioResponse)
                .collect(Collectors.toList());
    }

    // Obtener estadísticas
    public UsuarioStats getEstadisticas() {
        Long total = usuarioRepository.count();
        Long clientes = usuarioRepository.countByRol("cliente");
        Long admins = usuarioRepository.countByRol("admin");
        Long activos = usuarioRepository.countByEstado("activo");
        Long nuevosHoy = usuarioRepository.findUsuariosRecientes().stream()
                .filter(u -> u.getFechaRegistro().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .count();

        return new UsuarioStats(total, clientes, admins, activos, nuevosHoy);
    }

    // Clase interna para estadísticas
    public static class UsuarioStats {
        private Long total;
        private Long clientes;
        private Long admins;
        private Long activos;
        private Long nuevosHoy;

        public UsuarioStats(Long total, Long clientes, Long admins, Long activos, Long nuevosHoy) {
            this.total = total;
            this.clientes = clientes;
            this.admins = admins;
            this.activos = activos;
            this.nuevosHoy = nuevosHoy;
        }

        // Getters
        public Long getTotal() {
            return total;
        }

        public Long getClientes() {
            return clientes;
        }

        public Long getAdmins() {
            return admins;
        }

        public Long getActivos() {
            return activos;
        }

        public Long getNuevosHoy() {
            return nuevosHoy;
        }
    }
}