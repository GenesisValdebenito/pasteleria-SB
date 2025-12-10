package com.example.pasteleriasabores.pasteleria_sabores.service;

import com.example.pasteleriasabores.pasteleria_sabores.dto.PedidoDto;
import com.example.pasteleriasabores.pasteleria_sabores.dto.PedidoItemDto;
import com.example.pasteleriasabores.pasteleria_sabores.dto.PedidoRequest;
import com.example.pasteleriasabores.pasteleria_sabores.model.*;
import com.example.pasteleriasabores.pasteleria_sabores.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public Pedido crearPedido(Long usuarioId, PedidoRequest req) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEntrega(req.getDireccionEntrega());
        pedido.setFechaCreacion(LocalDateTime.now());

        AtomicInteger subtotal = new AtomicInteger(0);

        List<PedidoItem> items = req.getItems().stream().map(itemReq -> {

            Producto producto = productoRepository.findById(itemReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            PedidoItem item = new PedidoItem();
            item.setProducto(producto);
            item.setCantidad(itemReq.getCantidad());
            item.setPrecioUnitario(itemReq.getPrecioUnitario());
            item.setPedido(pedido);

            subtotal.addAndGet(item.getTotalItem()); // ← YA NO FALLA

            return item;

        }).toList();

        pedido.setItems(items);
        pedido.setSubtotal(subtotal.get());
        //pedido.setEnvio(req.getEnvio());
        //pedido.setDescuento(req.getDescuento());
        //pedido.setTotal(subtotal.get() + req.getEnvio() - req.getDescuento());

        return pedidoRepository.save(pedido);
    }

    // -------------------------

    public PedidoDto toDto(Pedido pedido) {
        PedidoDto dto = new PedidoDto();

        dto.setId(pedido.getId());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setEstado(pedido.getEstado());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setEnvio(pedido.getEnvio());
        dto.setDescuento(pedido.getDescuento());
        dto.setTotal(pedido.getTotal());
        dto.setDireccionEntrega(pedido.getDireccionEntrega());

        List<PedidoItemDto> items = pedido.getItems().stream().map(item -> {
            PedidoItemDto dtoItem = new PedidoItemDto();
            dtoItem.setProductoId(item.getProducto().getId());
            // dtoItem.setNombreProducto(item.getProducto().getNombre());
            dtoItem.setCantidad(item.getCantidad());
            dtoItem.setPrecioUnitario(item.getPrecioUnitario());
            // dtoItem.setTotalItem(item.getTotalItem());
            // dtoItem.setImagen(item.getProducto().getImagen());
            return dtoItem;
        }).toList();

        dto.setItems(items);
        return dto;
    }

    public List<Pedido> obtenerPedidosPorUsuario(Long usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }
}
