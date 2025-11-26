package com.example.pasteleriasabores.pasteleria_sabores.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pasteleriasabores.pasteleria_sabores.model.Rol;
import com.example.pasteleriasabores.pasteleria_sabores.model.Usuario;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Rol findByNombre(String nombre);
}

