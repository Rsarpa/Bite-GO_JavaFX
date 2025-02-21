package com.example.bitego_javafx.Model;

import jakarta.persistence.*;

import java.time.Year;
import java.util.Date;
@Entity
@Table(name="curso") //Asegurate de que coincide con el nombre de la tabla BD

public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_curso;

    @Column(name = "nombre_curso", nullable = false)
    private String nombre_curso;

    @Column(name = "anyo", nullable = false)
    private Year anyo;

    public Curso() {
    }

    public Curso(int id_curso, String nombre_curso, Year anyo) {
        this.id_curso = id_curso;
        this.nombre_curso = nombre_curso;
        this.anyo = anyo;
    }

    public int getId_curso() {
        return id_curso;
    }

    public void setId_curso(int id_curso) {
        this.id_curso = id_curso;
    }

    public String getNombre_curso() {
        return nombre_curso;
    }

    public void setNombre_curso(String nombre_curso) {
        this.nombre_curso = nombre_curso;
    }

    public Year getAnyo() {
        return anyo;
    }

    public void setAnyo(Year anyo) {
        this.anyo = anyo;
    }

    @Override
    public String toString() {
        return nombre_curso;
    }
}
