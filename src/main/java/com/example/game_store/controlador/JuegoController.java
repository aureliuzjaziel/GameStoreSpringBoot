package com.example.game_store.controlador;

import com.example.game_store.entidad.Juego;
import com.example.game_store.repositorio.JuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/juegos")  // ← ESTA LÍNEA DEFINE EL NOMBRE DE LA API
public class JuegoController {

    @Autowired
    private JuegoRepository juegoRepository;

    @GetMapping  // GET /api/juegos
    public List<Juego> obtenerTodos() {
        return juegoRepository.findAll();
    }

    @GetMapping("/{id}")  // GET /api/juegos/1
    public Juego obtenerPorId(@PathVariable Long id) {
        return juegoRepository.findById(id).orElse(null);
    }

    @PostMapping  // POST /api/juegos
    public Juego crear(@RequestBody Juego juego) {
        return juegoRepository.save(juego);
    }

    @PutMapping("/{id}")  // PUT /api/juegos/1
    public Juego actualizar(@PathVariable Long id, @RequestBody Juego juego) {
        juego.setId(id);
        return juegoRepository.save(juego);
    }

    @DeleteMapping("/{id}")  // DELETE /api/juegos/1
    public void eliminar(@PathVariable Long id) {
        juegoRepository.deleteById(id);
    }
}