package com.example.bitego_javafx.Model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "alergeno")
public class Alergeno {

    @Id
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "f_baja")
    private Date f_baja;

    @Column(name = "icono")
    private String icono;

    @ManyToMany(mappedBy = "alergenos", fetch = FetchType.EAGER)
    private Set<Bocadillo> bocadillos;

    public Alergeno() {}

    public Alergeno(String nombre, String descripcion, Date f_baja, String icono) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.f_baja = f_baja;
        this.icono = icono;
    }

    public Set<Bocadillo> getBocadillos() {
        return bocadillos;
    }

    public void setBocadillos(Set<Bocadillo> bocadillos) {
        this.bocadillos = bocadillos;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getF_baja() {
        return f_baja;
    }

    public void setF_baja(Date f_baja) {
        this.f_baja = f_baja;
    }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

}
