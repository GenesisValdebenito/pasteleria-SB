package com.example.pasteleriasabores.pasteleria_sabores.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasteleriasabores.pasteleria_sabores.model.Contacto;
import com.example.pasteleriasabores.pasteleria_sabores.repository.ContactoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContactoService {
    
    @Autowired
    private ContactoRepository contactoRepository;
    
    // Guardar nuevo contacto
    public Contacto save(Contacto contacto) {
        if (contacto.getFecha() == null) {
            contacto.setFecha(LocalDateTime.now());
        }
        if (contacto.getEstado() == null) {
            contacto.setEstado("pendiente");
        }
        return contactoRepository.save(contacto);
    }
    
    // Obtener todos los contactos
    public List<Contacto> findAll() {
        return contactoRepository.findAll();
    }
    
    // Obtener contacto por ID
    public Optional<Contacto> findById(Long id) {
        return contactoRepository.findById(id);
    }
    
    // Obtener contactos por estado
    public List<Contacto> findByEstado(String estado) {
        return contactoRepository.findByEstado(estado);
    }
    
    // Obtener contactos por email
    public List<Contacto> findByEmail(String email) {
        return contactoRepository.findByEmail(email);
    }
    
    // Obtener contactos pendientes
    public List<Contacto> findPendientes() {
        return contactoRepository.findByEstadoOrderByFechaDesc("pendiente");
    }
    
    // ✅ ACTUALIZADO: Obtener contactos recientes
    public List<Contacto> findContactosRecientes() {
        LocalDateTime fechaInicio = LocalDateTime.now().minusDays(30);
        return contactoRepository.findContactosRecientes(fechaInicio);
    }
    
    // Resto de métodos permanecen igual...
    public Contacto marcarComoLeido(Long id) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado con ID: " + id));
        contacto.setEstado("leido");
        return contactoRepository.save(contacto);
    }
    
    public Contacto marcarComoRespondido(Long id) {
        Contacto contacto = contactoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado con ID: " + id));
        contacto.setEstado("respondido");
        return contactoRepository.save(contacto);
    }
    
    public void deleteById(Long id) {
        if (!contactoRepository.existsById(id)) {
            throw new RuntimeException("Contacto no encontrado con ID: " + id);
        }
        contactoRepository.deleteById(id);
    }
    
    // Estadísticas de contactos
    public ContactoStats getEstadisticas() {
        Long total = contactoRepository.count();
        Long pendientes = contactoRepository.countByEstado("pendiente");
        Long leidos = contactoRepository.countByEstado("leido");
        Long respondidos = contactoRepository.countByEstado("respondido");
        
        return new ContactoStats(total, pendientes, leidos, respondidos);
    }
    
    // Buscar contactos por nombre
    public List<Contacto> buscarPorNombre(String nombre) {
        return contactoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Clase interna para estadísticas
    public static class ContactoStats {
        private Long total;
        private Long pendientes;
        private Long leidos;
        private Long respondidos;
        
        public ContactoStats(Long total, Long pendientes, Long leidos, Long respondidos) {
            this.total = total;
            this.pendientes = pendientes;
            this.leidos = leidos;
            this.respondidos = respondidos;
        }
        
        // Getters
        public Long getTotal() { return total; }
        public Long getPendientes() { return pendientes; }
        public Long getLeidos() { return leidos; }
        public Long getRespondidos() { return respondidos; }
    }
}
