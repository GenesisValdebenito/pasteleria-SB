package com.example.pasteleriasabores.pasteleria_sabores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.pasteleriasabores.pasteleria_sabores.model.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);
    
    // Verificar si existe un usuario por email
    boolean existsByEmail(String email);
    
    // Buscar usuarios por rol
    List<Usuario> findByRol(String rol);
    
    // Buscar usuarios por estado
    List<Usuario> findByEstado(String estado);
    
    // Buscar usuarios por rol y estado
    List<Usuario> findByRolAndEstado(String rol, String estado);
    
    // Buscar usuarios por nombre que contenga texto
    List<Usuario> findByNombreContainingIgnoreCase(String nombre);
    
    // Contar usuarios por rol
    Long countByRol(String rol);
    
    // Contar usuarios por estado
    Long countByEstado(String estado);
    
    // ✅ CORREGIDO: Buscar usuarios recientes (últimos 7 días)
    @Query("SELECT u FROM Usuario u WHERE u.fechaRegistro >= FUNCTION('DATE_SUB', CURRENT_DATE, 7) ORDER BY u.fechaRegistro DESC")
    List<Usuario> findUsuariosRecientes();
    
    // Buscar administradores
    @Query("SELECT u FROM Usuario u WHERE u.rol = 'admin' ORDER BY u.nombre")
    List<Usuario> findAdministradores();
}
