package com.example.pasteleriasabores.pasteleria_sabores.repository;

import org.springframework.stereotype.Repository;
import com.example.pasteleriasabores.pasteleria_sabores.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario
}
