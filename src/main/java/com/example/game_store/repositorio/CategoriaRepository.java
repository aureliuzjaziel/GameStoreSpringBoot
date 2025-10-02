package com.example.game_store.repositorio;

import com.example.game_store.entidad.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Método personalizado para buscar por nombre
    Categoria findByNombre(String nombre);

    // Método para verificar si existe una categoría por nombre
    boolean existsByNombre(String nombre);
}
