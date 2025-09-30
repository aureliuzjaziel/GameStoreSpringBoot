package com.example.game_store.entidad;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "juego_id")
    private Juego juego;

    public void setId(Long id) {
    }
}