package com.example.pasteleriasabores.pasteleria_sabores.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pasteleriasabores.pasteleria_sabores.model.Contacto;
import com.example.pasteleriasabores.pasteleria_sabores.service.ContactoService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactoController {
    
    @Autowired
    private ContactoService contactoService;
    
    // Enviar mensaje de contacto
    @PostMapping
    public ResponseEntity<Contacto> enviarMensaje(@RequestBody Contacto contacto) {
        try {
            Contacto savedContacto = contactoService.save(contacto);
            return ResponseEntity.ok(savedContacto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Obtener todos los mensajes (para admin)
    @GetMapping
    public ResponseEntity<List<Contacto>> getAllMensajes() {
        List<Contacto> contactos = contactoService.findAll();
        return ResponseEntity.ok(contactos);
    }
    
    // Obtener mensaje por ID
    @GetMapping("/{id}")
    public ResponseEntity<Contacto> getMensajeById(@PathVariable Long id) {
        Optional<Contacto> contacto = contactoService.findById(id);
        return contacto.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    // Obtener mensajes pendientes
    @GetMapping("/pendientes")
    public ResponseEntity<List<Contacto>> getMensajesPendientes() {
        List<Contacto> pendientes = contactoService.findPendientes();
        return ResponseEntity.ok(pendientes);
    }
    
    // Obtener mensajes por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Contacto>> getMensajesPorEstado(@PathVariable String estado) {
        List<Contacto> contactos = contactoService.findByEstado(estado);
        return ResponseEntity.ok(contactos);
    }
    
    // Marcar mensaje como leído
    @PutMapping("/{id}/leido")
    public ResponseEntity<Contacto> marcarComoLeido(@PathVariable Long id) {
        try {
            Contacto contacto = contactoService.marcarComoLeido(id);
            return ResponseEntity.ok(contacto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Marcar mensaje como respondido
    @PutMapping("/{id}/respondido")
    public ResponseEntity<Contacto> marcarComoRespondido(@PathVariable Long id) {
        try {
            Contacto contacto = contactoService.marcarComoRespondido(id);
            return ResponseEntity.ok(contacto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Eliminar mensaje
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMensaje(@PathVariable Long id) {
        try {
            contactoService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Obtener estadísticas (para admin)
    @GetMapping("/estadisticas")
    public ResponseEntity<ContactoService.ContactoStats> getEstadisticas() {
        ContactoService.ContactoStats stats = contactoService.getEstadisticas();
        return ResponseEntity.ok(stats);
    }
    
    // Buscar mensajes por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Contacto>> buscarPorNombre(@RequestParam String nombre) {
        List<Contacto> contactos = contactoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(contactos);
    }
}
