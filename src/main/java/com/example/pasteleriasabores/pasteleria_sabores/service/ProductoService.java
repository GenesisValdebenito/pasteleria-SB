package com.example.pasteleriasabores.pasteleria_sabores.service;

import com.example.pasteleriasabores.pasteleria_sabores.model.Producto;
import com.example.pasteleriasabores.pasteleria_sabores.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    // Método para generar un SKU aleatorio
    public String generateSKU() {
        return "SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Método para crear un nuevo producto
    public Producto createProducto(Producto producto) {
        producto.setSku(generateSKU()); // Generar el SKU
        return productoRepository.save(producto);  // Guardar el producto con SKU
    }

    // Método para obtener todos los productos
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    // Método para obtener un producto por su ID
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public Producto updateProducto(Long id, Producto detalles) {
    Producto producto = productoRepository.findById(id).orElse(null);

    if (producto != null) {
        producto.setSku(detalles.getSku());
        producto.setNombre(detalles.getNombre());
        producto.setCategoria(detalles.getCategoria());
        producto.setImagen(detalles.getImagen());
        producto.setDescripcion(detalles.getDescripcion());
        producto.setStock(detalles.getStock());
        producto.setPrecio(detalles.getPrecio());
        producto.setDestacado(detalles.getDestacado());
        producto.setOferta(detalles.getOferta());
        producto.setTipo(detalles.getTipo());
        producto.setTamano(detalles.getTamano());

        return productoRepository.save(producto);
    }
    return null;
}

    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }
}


