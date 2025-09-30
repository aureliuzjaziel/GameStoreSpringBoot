package com.example.game_store.controlador;

import com.example.game_store.entidad.Carrito;
import com.example.game_store.repositorio.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/carrito")
@RestController
public class CarritoController {

    @Autowired
    private CarritoRepository carritoRepository;

    @GetMapping("/usuario/{usuarioId}")
    public List<Carrito> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    @PostMapping
    public Carrito agregar(@RequestBody Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @PutMapping("/{id}")
    public Carrito actualizar(@PathVariable Long id, @RequestBody Carrito carrito) {
        carrito.setId(id);
        return carritoRepository.save(carrito);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        carritoRepository.deleteById(id);
    }

    @DeleteMapping("/usuario/{usuarioId}")
    public void limpiarCarrito(@PathVariable Long usuarioId) {
        carritoRepository.deleteByUsuarioId(usuarioId);
    }

    @GetMapping("/{id}")
    public Carrito obtenerPorId(@PathVariable Long id) {
        return carritoRepository.findById(id).orElse(null);
    }
}