package com.example.game_store.repositorio;

import com.example.game_store.entidad.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long> {

    // Buscar juegos por categoría
    List<Juego> findByCategoriaId(Long categoriaId);

    // Buscar juegos por nombre (contiene)
    List<Juego> findByNombreContainingIgnoreCase(String nombre);

    // Buscar juegos con stock disponible
    List<Juego> findByStockGreaterThan(Integer stock);
}