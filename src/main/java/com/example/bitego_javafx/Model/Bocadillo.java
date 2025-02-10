package com.example.bitego_javafx.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bocadillo")
public class Bocadillo {

    @Id
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "tipo", nullable = false)
    private String tipo;
    @Column(name = "descripcion", nullable = false)
    private String descripcion;
    @Column(name = "precio_base", nullable = false)
    private Float precio_base;
    @Column(name = "dia_asociado", nullable = false)
    private int dia_asociado;

    public Bocadillo(String nombre, String tipo, String descripcion, Float precio_base, int dia_asociado) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precio_base = precio_base;
        this.dia_asociado = dia_asociado;
    }

    public Bocadillo() {}

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio_base(Float precio_base) {
        this.precio_base = precio_base;
    }

    public void setDia_asociado(int dia_asociado) {
        this.dia_asociado = dia_asociado;
    }
}
