package com.example.game_store.repositorio;

import com.example.game_store.entidad.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JuegoRepository extends JpaRepository<Juego, Long> {
    List<Juego> findByCategoriaId(Long categoriaId);
    List<Juego> findByNombreContaining(String nombre);
}