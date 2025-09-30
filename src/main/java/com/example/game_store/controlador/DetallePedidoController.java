package com.example.game_store.controlador;

import com.example.game_store.entidad.DetallePedido;
import com.example.game_store.repositorio.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RequestMapping("/detallePedido")
@RestController

public class DetallePedidoController {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @GetMapping("/pedido/{pedidoId}")
    public List<DetallePedido> obtenerPorPedido(@PathVariable Long pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    @GetMapping("/{id}")
    public DetallePedido obtenerPorId(@PathVariable Long id) {
        return detallePedidoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public DetallePedido crear(@RequestBody DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    @PutMapping("/{id}")
    public DetallePedido actualizar(@PathVariable Long id, @RequestBody DetallePedido detallePedido) {
        detallePedido.setId(id);
        return detallePedidoRepository.save(detallePedido);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        detallePedidoRepository.deleteById(id);
    }
}