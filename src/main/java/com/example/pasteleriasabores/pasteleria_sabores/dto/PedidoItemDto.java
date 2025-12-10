package com.example.pasteleriasabores.pasteleria_sabores.dto;

import lombok.Data;

@Data
public class PedidoItemDto {
    private Long productoId;
    private String nombreProducto;
    private Integer cantidad;
    private Integer precioUnitario;
    private Integer totalItem;
    private String imagen;
}

