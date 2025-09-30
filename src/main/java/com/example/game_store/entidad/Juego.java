package com.example.game_store.entidad;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "juego")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private double precio;
    private String imagen;
    private Integer stock;
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categorianombre;

    @OneToMany(mappedBy = "juego")
    private List<DetallePedido> detallesPedido;

    @OneToMany(mappedBy = "juego")
    private List<Carrito> carritoItems;

    public void setId(Long id) {
    }
}
