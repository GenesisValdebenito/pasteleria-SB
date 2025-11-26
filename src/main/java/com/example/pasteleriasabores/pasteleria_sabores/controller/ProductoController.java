package com.example.pasteleriasabores.pasteleria_sabores.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.pasteleriasabores.pasteleria_sabores.model.Producto;
import com.example.pasteleriasabores.pasteleria_sabores.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:3000")  // Para permitir React
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // ------------------------------
    // LISTAR (USER + ADMIN)
    // ------------------------------
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> productos = productoService.getAllProductos();
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable("id") Long id) {
        Producto producto = productoService.getProductoById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    // ------------------------------
    // CREAR (SOLO ADMIN)
    // ------------------------------
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.createProducto(producto);
        return ResponseEntity.ok(nuevoProducto);
    }

    // ------------------------------
    // ACTUALIZAR (SOLO ADMIN)
    // ------------------------------
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto productoDetails) {
        return productoService.updateProducto(id, productoDetails);
    }

    // ------------------------------
    // ELIMINAR (SOLO ADMIN)
    // ------------------------------
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
    }
}
