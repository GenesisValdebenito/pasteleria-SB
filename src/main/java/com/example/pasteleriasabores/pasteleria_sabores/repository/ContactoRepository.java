package com.example.pasteleriasabores.pasteleria_sabores.repository;

import com.example.pasteleriasabores.pasteleria_sabores.model.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    
    // Buscar contactos por estado
    List<Contacto> findByEstado(String estado);
    
    // Buscar contactos por email
    List<Contacto> findByEmail(String email);
    
    // Buscar contactos pendientes
    List<Contacto> findByEstadoOrderByFechaDesc(String estado);
    
    // Contar contactos por estado
    Long countByEstado(String estado);
    
    // ✅ CORREGIDO: Buscar contactos recientes (últimos 30 días)
    @Query("SELECT c FROM Contacto c WHERE c.fecha >= :fechaInicio ORDER BY c.fecha DESC")
    List<Contacto> findContactosRecientes(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    // Buscar por nombre que contenga texto
    List<Contacto> findByNombreContainingIgnoreCase(String nombre);
}
