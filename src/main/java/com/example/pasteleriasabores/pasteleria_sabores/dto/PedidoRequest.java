package com.example.pasteleriasabores.pasteleria_sabores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {

    private String direccionEntrega;
    private String metodoPago;      // "TARJETA", "EFECTIVO", etc.
    private Integer costoEnvio;
    private Integer totalOriginal;
    private Integer totalFinal;

    private List<PedidoItemRequest> items;
}
