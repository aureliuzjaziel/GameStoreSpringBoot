package com.example.game_store.repositorio;

import com.example.game_store.entidad.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}