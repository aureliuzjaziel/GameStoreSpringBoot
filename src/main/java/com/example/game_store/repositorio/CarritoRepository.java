package com.example.game_store.repositorio;

import com.example.game_store.entidad.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    List<Carrito> findByUsuarioId(Long usuarioId);
    void deleteByUsuarioId(Long usuarioId);
}