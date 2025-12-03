package com.example.pasteleriasabores.pasteleria_sabores.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pasteleriasabores.pasteleria_sabores.model.Categoria;
import com.example.pasteleriasabores.pasteleria_sabores.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoryRepository;

    public List<Categoria> findAll() {
        return categoryRepository.findAll();
    }

    public Categoria findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Categoria save(Categoria category) {
        return categoryRepository.save(category);
    }

    public Categoria update(Long id, Categoria data) {
        Categoria c = categoryRepository.findById(id).orElse(null);
        if (c == null) return null;

        c.setNombre(data.getNombre());
        c.setDescripcion(data.getDescripcion());

        return categoryRepository.save(c);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
