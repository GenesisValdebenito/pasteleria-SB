package com.example.pasteleriasabores;

import com.example.pasteleriasabores.pasteleria_sabores.model.Producto;
import com.example.pasteleriasabores.pasteleria_sabores.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @Test
    void crearProductoTest() {

        Producto p = new Producto();
            p.setSku("TK-001");
            p.setNombre("Torta Chocolate");
            p.setCategoria("Tortas");
            p.setImagen("torta.png");
            p.setDescripcion("Rica torta");
            p.setStock(10);
            p.setPrecio(15990.0);
            p.setDestacado(true);
            p.setOferta(false);
            p.setTipo("cuadrada");
            p.setTamano("mediana");

        Producto guardado = productoService.createProducto(p);

        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getNombre()).isEqualTo("Torta Chocolate");
    }
}
