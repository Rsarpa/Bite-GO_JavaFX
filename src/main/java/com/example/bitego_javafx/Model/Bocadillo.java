package com.example.bitego_javafx.Model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "bocadillo")
public class Bocadillo {

    @Id
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio_base")
    private Float precio_base;

    @Column(name = "dia_asociado")
    private int dia_asociado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "alergeno_bocadillo",
            joinColumns = @JoinColumn(name = "id_bocadillo", referencedColumnName = "nombre"),
            inverseJoinColumns = @JoinColumn(name = "id_alergeno", referencedColumnName = "nombre")
    )
    private Set<Alergeno> alergenos;

    public Set<Alergeno> getAlergenos() {
        return alergenos;
    }

    // Relación OneToMany con Pedido (un Bocadillo puede estar en muchos pedidos)
    @OneToMany(mappedBy = "bocadillo")
    private Set<PedidoBocadillo> pedidos;


    public Bocadillo(String nombre, String tipo, String descripcion, Float precio_base, int dia_asociado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio_base = precio_base;
        this.dia_asociado = dia_asociado;
    }

    public Bocadillo() {}

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Float getPrecio_base() { return precio_base; }
    public void setPrecio_base(Float precio_base) { this.precio_base = precio_base; }
    public int getDia_asociado() { return dia_asociado; }
    public void setDia_asociado(int dia_asociado) { this.dia_asociado = dia_asociado; }

    public Set<PedidoBocadillo> getPedidos() { return pedidos; }
    public void setPedidos(Set<PedidoBocadillo> pedidos) { this.pedidos = pedidos; }
}
